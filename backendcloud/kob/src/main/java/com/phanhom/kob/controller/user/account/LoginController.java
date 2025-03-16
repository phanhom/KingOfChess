package com.phanhom.kob.controller.user.account;

import com.phanhom.kob.service.impl.user.account.LoginServiceImpl;
import com.phanhom.kob.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
//@CrossOrigin(origins = "http://localhost:5173/user/account/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/api/user/account/token")
    public Map<String, String> getToken(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username, password);
    }
}
