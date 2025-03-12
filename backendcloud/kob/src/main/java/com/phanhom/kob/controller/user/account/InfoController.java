package com.phanhom.kob.controller.user.account;

import com.phanhom.kob.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info")
    public Map<String, String> getinfo() {
        return infoService.getinfo();
    }

    @PostMapping("/user/account/modifyinfo")
    public Map<String, String> modifyInfo(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String photo = map.get("photo");
        String description = map.get("description");
//        System.out.println("username: " + username + " photo: " + photo);
        return infoService.modifyInfo(username, photo, description);
    }
}
