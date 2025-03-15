package com.phanhom.kob.service.user.bot;

import java.io.IOException;
import java.util.Map;

public interface UpdateService {
    Map<String, String> update(Map<String, String> data) throws IOException;
}
