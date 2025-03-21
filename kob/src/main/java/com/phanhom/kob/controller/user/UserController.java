package com.phanhom.kob.controller.user;

import com.phanhom.kob.mapper.UserMapper;
import com.phanhom.kob.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/all")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("/user/{userId}")
    public User getById(@PathVariable Integer userId) {
        return userMapper.selectById(userId);
    }

    //post
    @GetMapping("/user/add/{userId}/{username}/{password}")
    public String add(@PathVariable Integer userId, @PathVariable String username, @PathVariable String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String newPassword = passwordEncoder.encode(password);
        User user = new User(userId, username, newPassword, "https://userpic.codeforces.org/no-title.jpg");
        userMapper.insert(user);
        return "add user success";
    }

    // post
    @GetMapping("/user/delete/{userId}")
    public String delete(@PathVariable Integer userId) {
        userMapper.deleteById(userId);
        return "delete user success";
    }

}
