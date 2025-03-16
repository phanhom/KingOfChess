package com.phanhom.kob.controller.user.account;

import com.phanhom.kob.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin
@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/api/user/account/register")
    public Map<String, String> register(@RequestBody Map<String, String> map) throws IOException {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
//        System.out.println("username: " + username + " password: " + password);
        return registerService.register(username, password, confirmedPassword);
    }
}
