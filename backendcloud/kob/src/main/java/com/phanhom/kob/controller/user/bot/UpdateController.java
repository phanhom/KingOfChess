package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @PostMapping("/user/bot/update")
    public Map<String, String> update(@RequestBody Map<String, String> map) {
//        System.out.println(map);
        return updateService.update(map);
    }
}
