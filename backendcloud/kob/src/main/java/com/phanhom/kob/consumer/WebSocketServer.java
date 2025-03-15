package com.phanhom.kob.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.phanhom.kob.mapper.UserMapper;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//就是websocket最大值，别让服务器过度承载?

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    // 每个 id 对应 websocket
    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

//    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private ScheduledExecutorService scheduler;
    private User user;
    private Session session = null;
    private Game game = null;
    private static RestTemplate restTemplate;
    private static final String addPlayer = "http://127.0.0.1:3003/player/add";
    private static final String removePlayer = "http://127.0.0.1:3003/player/remove";

    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        // 限制最大数目????
        this.session = session;

        Integer userId = JwtUtil.JWT2UserID(token);
        this.user = userMapper.selectById(userId);

        if(user != null) {
            users.put(userId, this);
            startScheduledTask(userId);
        } else {
            this.session.close();
        }
    }

    private void startScheduledTask(Integer userId) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            JSONObject resp = new JSONObject();
            JSONObject data = new JSONObject();
            resp.put("event", "heartbeat");
            data.put("online_count", users.size());
            data.put("timestamp", System.currentTimeMillis());
            resp.put("data", data);
            users.get(userId).sendMessage(resp.toJSONString());
        }, 0, 5000, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @OnClose
    public void onClose() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        // 关闭链接
        if(this.user != null) {
            // 判定输了
            whiteFlag();
            users.remove(this.user.getId());
            MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
            data.add("user_id", this.user.getId().toString());
            restTemplate.postForObject(removePlayer, data, String.class);
        }
    }

    private void whiteFlag() {
        if(this.game == null) return;
        game.whiteFlag(this.user.getId());
    }

    // ????
    public static void settlement(Integer winnerId, Integer loserId) {
        User winner = userMapper.selectById(winnerId);
        User loser = userMapper.selectById(loserId);
        int winner_score = winner.getRating();
        int loser_score = loser.getRating();
        int diff = winner_score - loser_score;
        int result = (loser_score - winner_score) / 50;
        if(diff >= 0) {
            result = 9 + Math.max(-4, result);
        } else {
            result = 9 + Math.min(6, result);
        }
        winner.setRating(Math.max(500, Math.min(3000, winner_score + result)));
        loser.setRating(Math.max(500, Math.min(3000, loser_score - result)));
        userMapper.updateById(winner);
        userMapper.updateById(loser);
        JSONObject res = new JSONObject();
        res.put("event", "result");
        res.put("change", result);
        // ???? 是不是null，要加判断
        users.get(winnerId).sendMessage(res.toJSONString());
        users.get(loserId).sendMessage(res.toJSONString());
    }


    public void startGame(Integer aId, Integer bId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        game = new Game(16, 16, 20, a.getId(), b.getId());
        users.get(a.getId()).game = game;
        users.get(b.getId()).game = game;

        game.start();

        JSONObject resA = new JSONObject();
        resA.put("event", "matched");
        resA.put("opponent_username", b.getUsername());
        resA.put("opponent_photo", b.getPhoto());
        resA.put("opponent_rating", b.getRating());
        resA.put("game_map", game.getG());  // 地图
        users.get(a.getId()).sendMessage(resA.toJSONString());

        JSONObject resB = new JSONObject();
        resB.put("event", "matched");
        resB.put("opponent_username", a.getUsername());
        resB.put("opponent_photo", a.getPhoto());
        resB.put("opponent_rating", a.getRating());
        resB.put("game_map", game.getG());  // 地图
        users.get(b.getId()).sendMessage(resB.toJSONString());
    }

    private void startMatching() {
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayer, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayer, data, String.class);
    }

    private void move(Integer direction) {
        if(game.getP1().getId().equals(this.user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getP2().getId().equals(this.user.getId())) {
            game.setNextStepB(direction);
        }
    }

    private void shoot() {
        game.sendBullet(this.user.getId());
    }

    private void sendChatMessage(Integer fromUserId, Integer toUserId, String message) {
        JSONObject chat = new JSONObject();
        chat.put("event", "chat_message");
        chat.put("chat", message);
        if(users.get(toUserId) == null ) return;
        System.out.println("fromUserId" + fromUserId + " toUserId" + toUserId);
        users.get(toUserId).sendMessage(chat.toJSONString());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)) {
            startMatching();
        } else if("stop-matching".equals(event)) {
            stopMatching();
        } else if("move".equals(event)) {
            move(data.getInteger("direction"));
        } else if("shoot".equals(event)) {
            shoot();
        } else if("message".equals(event)) {
            sendChatMessage(data.getInteger("from_user_id"),
                    data.getInteger("to_user_id"),
                    data.getString("message"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        // 发送消息
        if(this.session == null) return;
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}