package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.pojo.Bot;
import com.phanhom.kob.service.user.bot.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class GetController {

    @Autowired
    private GetService getService;

    @PostMapping("/api/user/bot/get")
    public List<Bot> get() {
        return getService.get();
    }

    @PostMapping("/api/user/bot/getone")
    public Bot getone(Integer botId) {
        return getService.getone(botId);
    }
}
