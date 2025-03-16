package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
public class RemoveController {

    @Autowired
    private RemoveService removeService;

    @PostMapping("/api/user/bot/remove")
    public Map<String, String> remove(@RequestBody Map<String, String> data) {
        return removeService.remove(data);
    }
}
