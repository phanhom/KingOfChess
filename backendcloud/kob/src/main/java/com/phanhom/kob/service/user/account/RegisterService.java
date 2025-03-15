package com.phanhom.kob.service.user.account;

import java.io.IOException;
import java.util.Map;

public interface RegisterService {
    Map<String, String> register(String username, String password, String confirmedPassword) throws IOException;
}
