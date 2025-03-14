package com.phanhom.botrunningsystem.controller;

import com.phanhom.botrunningsystem.service.BotRunningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotRunningController {

    @Autowired
    private BotRunningService botRunningService;

    @PostMapping("/bot/add")
    public String addBot(@RequestParam MultiValueMap<String, String> map) {
        System.out.println("add bot");
        Integer userId = Integer.parseInt(map.getFirst("user_id"));
        String botCode = map.getFirst("bot_code");
        String input = map.getFirst("input");
        return botRunningService.addBot(userId, botCode, input);
    }

}
