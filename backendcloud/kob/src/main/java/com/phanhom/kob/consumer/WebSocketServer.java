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
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

//就是websocket最大值，别让服务器过度承载?

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    // 每个 id 对应 websocket
    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

//    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
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
        this.session = session;

        Integer userId = JwtUtil.JWT2UserID(token);
        this.user = userMapper.selectById(userId);

        if(user != null) {
            users.put(userId, this);
            JSONObject resp = new JSONObject();
            resp.put("event", "update_online_count");
            resp.put("online_count", users.size());
            users.get(userId).sendMessage(resp.toJSONString());
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
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
    public void settlement(Integer winnerId, Integer loserId) {
        User winner = userMapper.selectById(winnerId);
        User loser = userMapper.selectById(loserId);
        int winner_score = winner.getRating();
        int loser_score = loser.getRating();
        int diff = winner_score - loser_score;
        int result = (int) Math.exp(-diff) * 9;
        if(diff >= 0) {
            result = Math.max(3, result);
            winner.setRating(winner_score + result);
            loser.setRating(loser_score - result);
            userMapper.updateById(winner);
            userMapper.updateById(loser);
        } else {
            diff *= -1;
            result = result * -1 + 18;
            result = Math.max(15, result);
            winner.setRating(winner_score + result);
            loser.setRating(loser_score - result);
            userMapper.updateById(winner);
            userMapper.updateById(loser);
        }
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
        resA.put("game_map", game.getG());  // 地图
        users.get(a.getId()).sendMessage(resA.toJSONString());

        JSONObject resB = new JSONObject();
        resB.put("event", "matched");
        resB.put("opponent_username", a.getUsername());
        resB.put("opponent_photo", a.getPhoto());
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
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        // 发送消息
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}