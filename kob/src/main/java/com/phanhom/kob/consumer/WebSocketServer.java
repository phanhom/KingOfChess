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

    final private static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null;
    private Game game = null;

    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
//        System.out.println("websocket online");

        Integer userId = JwtUtil.JWT2UserID(token);
        this.user = userMapper.selectById(userId);

        if(user != null) {
            users.put(userId, this);
//            System.out.println(users);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
//        System.out.println("websocket offline");
        if(this.user != null) {
            // 判定输了?
            whiteFlag();
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
    }

    private void whiteFlag() {
        if(this.game == null) return;
        game.whiteFlag(this.user.getId());
    }

    private void startMatching() {
//        System.out.println("start matching");
        matchPool.add(this.user);
        while(matchPool.size() >= 2) {
            Iterator<User> it = matchPool.iterator();
            User a = it.next(), b = it.next();
            matchPool.remove(a);
            matchPool.remove(b);

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
    }

    private void stopMatching() {
//        System.out.println("stop matching");
        matchPool.remove(this.user);
    }

    private void move(Integer direction) {
//        System.out.println(game.getP1().getId());
//        System.out.println(game.getP2().getId());
//        System.out.println(this.user.getId());
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
//        System.out.println("websocket receive message: " + message);

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