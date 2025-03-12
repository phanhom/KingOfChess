package com.phanhom.kob.service.impl.pk;

import com.phanhom.kob.service.pk.StartGameService;
import org.springframework.stereotype.Service;
import com.phanhom.kob.consumer.WebSocketServer;


@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        WebSocketServer.users.get(aId).startGame(aId, bId);
//        WebSocketServer.users.get(bId).startGame(aId, bId);
        return "start game success";
    }
}
