package com.phanhom.kob.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phanhom.kob.mapper.UserMapper;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.impl.utils.UserDetailsImpl;
import com.phanhom.kob.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> getinfo() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> res = new HashMap<>();
        res.put("error_message", "success");
        res.put("id", user.getId().toString());
        res.put("username", user.getUsername());
        res.put("photo", user.getPhoto());
        res.put("rating", user.getRating().toString());
        res.put("description", user.getDescription());
        return res;
    }

    @Override
    public Map<String, String> modifyInfo(String username, String photo, String description) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> res = new HashMap<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User targetUser = userMapper.selectOne(queryWrapper);
        if(targetUser != null && !Objects.equals(targetUser.getId(), user.getId())) {
            res.put("error_message", "用户名已被他人占用");
            return res;
        }

        if(username == null) {
            res.put("error_message", "用户名不能为空");
            return res;
        }

        if (username.length() > 20 || username.length() < 3) {
            res.put("error_message", "用户名长度不能大于20, 不能小于3");
            return res;
        }

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            res.put("error_message", "用户名只能包含字母和数字");
            return res;
        }

        if(description != null && description.length() > 80) {
            res.put("error_message", "描述最多80个字符");
            return res;
        }

        if(description == null || description.length() == 0) {
            description = "-";
        }

        user.setUsername(username);
        user.setPhoto(photo);
        user.setDescription(description);
        userMapper.updateById(user);

        res.put("error_message", "success");
        return res;
    }
}
