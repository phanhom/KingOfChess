package com.phanhom.kob.service.impl.user.bot;

import com.phanhom.kob.mapper.BotMapper;
import com.phanhom.kob.pojo.Bot;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.impl.utils.UserDetailsImpl;
import com.phanhom.kob.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();


        String botName = data.get("botName");
        String description = data.get("description");
        String content = data.get("content");
        Integer botId = Integer.parseInt(data.get("botId"));

        Map<String, String> map = new HashMap<>();

        Bot bot = botMapper.selectById(botId);
        if(bot == null) {
            map.put("error_message", "Bot不存在或已被删除");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限修改");
            return map;
        }

        if(botName == null || botName.length() == 0) {
            map.put("error_message", "Bot名字不能为空");
            return map;
        }

        if(botName.length() > 16) {
            map.put("error_message", "Bot名字不能超过16");
            return map;
        }

        if(description == null || description.length() == 0) {
            description = "-";
        }

        if(description.length() > 100) {
            map.put("error_message", "Bot描述不能超过100");
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


        bot.setBotName(botName);
        bot.setContent(content);
        bot.setDescription(description);
        bot.setModifytime(new Date());
        botMapper.updateById(bot);

        map.put("error_message", "success");
        return map;
    }
}
