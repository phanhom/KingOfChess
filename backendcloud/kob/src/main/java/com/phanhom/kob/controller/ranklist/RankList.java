package com.phanhom.kob.controller.ranklist;

import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.ranklist.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RankList {

    @Autowired
    private RankListService rankListService;

    @GetMapping("/user/ranklist")
    public List<User> ranklist() {
        List<User> ans = rankListService.ranklist();
        return ans;
    }
}
