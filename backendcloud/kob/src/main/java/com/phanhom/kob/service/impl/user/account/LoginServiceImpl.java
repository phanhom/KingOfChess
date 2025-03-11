package com.phanhom.kob.service.impl.user.account;

import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.impl.utils.UserDetailsImpl;
import com.phanhom.kob.service.user.account.LoginService;
import com.phanhom.kob.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {

        // 加密
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 验证登录，登录失败会自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 取出登录用户
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        // 创建JWT token
        String Jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String, String> res = new HashMap<>();
        res.put("error_message", "success");
        res.put("token", Jwt);

        return res;
    }
}
