package com.phanhom.kob.service.user.bot;

import com.phanhom.kob.pojo.Bot;

import java.util.List;
import java.util.Map;

public interface GetService {
    List<Bot> get();
    Bot getone(Integer botId);
}
