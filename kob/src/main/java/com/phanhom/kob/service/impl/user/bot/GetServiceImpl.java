package com.phanhom.kob.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phanhom.kob.mapper.BotMapper;
import com.phanhom.kob.pojo.Bot;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.impl.utils.UserDetailsImpl;
import com.phanhom.kob.service.user.bot.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GetServiceImpl implements GetService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public List<Bot> get() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper botQueryWrapper = new QueryWrapper<>();
        botQueryWrapper.eq("user_id", user.getId());
        List<Bot> bots = botMapper.selectList(botQueryWrapper);

        return bots;
    }

    @Override
    public Bot getone(Integer botId) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Bot> botQueryWrapper = new QueryWrapper<>();
        botQueryWrapper.eq("id", botId);
        Bot bot = botMapper.selectOne(botQueryWrapper);
        if (bot == null) {
            return null;
        }
        if (!bot.getUserId().equals(user.getId())) {
            return null;
        }
        return bot;
    }
}
