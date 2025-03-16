package com.phanhom.kob.service.impl.ranklist;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public List<User> ranklistpage(Integer page) {
        IPage<User> userIPage = new Page<>(page, 20);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "username", "rating", "description")
                .ne("rating", 1500)
                .orderByDesc("rating");
        List<User> userList = userMapper.selectPage(userIPage, queryWrapper).getRecords();

        return userList;
    }

    @Override
    public Integer count() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("rating", 1500);

        return Math.toIntExact(userMapper.selectCount(queryWrapper));
    }
}
