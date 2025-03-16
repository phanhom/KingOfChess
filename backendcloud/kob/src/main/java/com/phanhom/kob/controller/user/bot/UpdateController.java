package com.phanhom.kob.controller.user.bot;

import com.phanhom.kob.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin
@RestController
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @PostMapping("/api/user/bot/update")
    public Map<String, String> update(@RequestBody Map<String, String> map) throws IOException {
        return updateService.update(map);
    }
}
