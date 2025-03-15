package com.phanhom.kob.controller.user.account;

import com.phanhom.kob.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping("/user/account/photo/userid")
    public Map<String, String> getuserphoto(@RequestBody Map<String, String> map) {
        Integer id = Integer.parseInt(map.get("id"));
        return infoService.getuserphoto(id);
    }

    @PostMapping("/user/account/modifyinfo")
    public Map<String, String> modifyInfo(@RequestBody Map<String, String> map) throws IOException {
        String username = map.get("username");
        String photo = map.get("photo");
        String description = map.get("description");
//        System.out.println("username: " + username + " photo: " + photo);
        return infoService.modifyInfo(username, photo, description);
    }
}
