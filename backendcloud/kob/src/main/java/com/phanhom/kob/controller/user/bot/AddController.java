package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin
@RestController
public class AddController {

    @Autowired
    private AddService addService;

    @PostMapping("/api/user/bot/add")
    public Map<String, String> add(@RequestBody Map<String, String> map) throws IOException {
        return addService.add(map);
    }
}
