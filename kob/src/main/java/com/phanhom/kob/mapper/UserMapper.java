package com.phanhom.kob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phanhom.kob.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
