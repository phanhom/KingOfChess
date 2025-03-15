package com.phanhom.kob.service.user.account;

import java.util.Map;

public interface InfoService {
    Map<String, String> getinfo();
    Map<String, String> getuserphoto(Integer id);
    Map<String, String> modifyInfo(String username, String photo, String description);
}
