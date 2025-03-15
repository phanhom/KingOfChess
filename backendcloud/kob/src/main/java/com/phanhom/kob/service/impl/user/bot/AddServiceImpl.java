package com.phanhom.kob.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phanhom.kob.mapper.BotMapper;
import com.phanhom.kob.pojo.Bot;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.impl.utils.UserDetailsImpl;
import com.phanhom.kob.service.user.bot.AddService;
import com.phanhom.kob.utils.OkHttpToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) throws IOException {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        // 防止一个人添加过多bots
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<Bot> bots = botMapper.selectList(queryWrapper);
        if (bots.size() >= 5) {
            map.put("error_message", "每个用户最多只能创建5个Bot");
            return map;
        }

        String botName = data.get("botName");
        String description = data.get("description");
        String content = data.get("content");

        if(botName == null || botName.length() == 0) {
            map.put("error_message", "Bot名字不能为空");
            return map;
        }

        if(botName.length() > 16) {
            map.put("error_message", "Bot名字不能超过16");
            return map;
        }
        if(!OkHttpToken.checkText(botName)) {
            map.put("error_message", "Bot名含有敏感词");
            return map;
        }

        if(description == null || description.length() == 0) {
            description = "-";
        }

        if(description.length() > 100) {
            map.put("error_message", "Bot描述不能超过100");
            return map;
        }
        if(!OkHttpToken.checkText(description)) {
            map.put("error_message", "简介含有敏感词");
            return map;
        }

        if(content == null || content.length() == 0) {
            map.put("error_message", "Bot Code不能为空");
            return map;
        }

        if(content.length() > 10000) {
            map.put("error_message", "Bot Code长度不能超过10000");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), botName, description, content, 1500, now, now);
        botMapper.insert(bot);

        map.put("error_message", "success");

        return map;
    }
}
