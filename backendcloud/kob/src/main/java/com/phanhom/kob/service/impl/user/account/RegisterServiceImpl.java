package com.phanhom.kob.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phanhom.kob.mapper.UserMapper;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.user.account.RegisterService;
import com.phanhom.kob.utils.OkHttpToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl  implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (username == null || password == null || confirmedPassword == null) {
            map.put("error_message", "用户名或密码不能为空");
            return map;
        }
        username = username.trim();

        if (username.length() == 0 || password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "用户名或密码不能为空");
            return map;
        }

        if (username.length() > 20 || username.length() < 3) {
            map.put("error_message", "用户名长度不能大于20, 不能小于3");
            return map;
        }

        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "用户名已被注册");
            return map;
        }

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            map.put("error_message", "用户名只能包含字母和数字");
            return map;
        }

        if(!OkHttpToken.checkText(username)) {
            map.put("error_message", "用户名含有敏感词");
            return map;
        }

        if (password.length() < 6) {
            map.put("error_message", "密码长度不能小于6");
            return map;
        }

        if (password.length() > 20 || confirmedPassword.length() > 20) {
            map.put("error_message", "密码长度不能大于20");
            return map;
        }

        if (!password.matches("^[a-zA-Z0-9.!*&@#]+$")) {
            map.put("error_message", "密码只能包含字母和数字和.!*&@#");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://userpic.codeforces.org/no-title.jpg";
        User user = new User(null, username, encodedPassword, photo, 1500, "-");
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }
}
