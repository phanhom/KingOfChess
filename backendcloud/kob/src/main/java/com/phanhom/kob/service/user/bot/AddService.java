package com.phanhom.kob.service.user.bot;

import java.io.IOException;
import java.util.Map;

public interface AddService {
    Map<String, String> add(Map<String, String> data) throws IOException;
}
