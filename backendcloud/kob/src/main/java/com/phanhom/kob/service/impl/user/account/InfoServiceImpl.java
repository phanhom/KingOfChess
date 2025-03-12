package com.phanhom.kob.service.impl.user.account;

import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.impl.utils.UserDetailsImpl;
import com.phanhom.kob.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {

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
        return res;
    }
}
