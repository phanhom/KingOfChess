package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddController {

    @Autowired
    private AddService addService;

    @PostMapping("/user/bot/add")
    public Map<String, String> add(@RequestParam Map<String, String> map) {
        System.out.println(map);
        return addService.add(map);
    }
}
