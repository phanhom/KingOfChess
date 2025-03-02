package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.pojo.Bot;
import com.phanhom.kob.service.user.bot.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetController {

    @Autowired
    private GetService getService;

    @PostMapping("/user/bot/get")
    public List<Bot> get() {
        return getService.get();
    }
}
