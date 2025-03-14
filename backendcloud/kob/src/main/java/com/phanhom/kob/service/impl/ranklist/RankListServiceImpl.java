package com.phanhom.kob.service.impl.ranklist;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phanhom.kob.mapper.UserMapper;
import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.ranklist.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankListServiceImpl implements RankListService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> ranklist() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "username", "rating", "description")
                .orderByDesc("rating");
        return userMapper.selectList(queryWrapper);
    }
}
