package com.phanhom.kob.controller.ranklist;

import com.phanhom.kob.pojo.User;
import com.phanhom.kob.service.ranklist.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RankList {

    @Autowired
    private RankListService rankListService;

//    @GetMapping("/user/ranklist")
//    public List<User> ranklist() {
//        List<User> ans = rankListService.ranklist();
//        return ans;
//    }

    @GetMapping("/user/ranklist")
    public List<User> ranklistpage(@RequestParam Map<String, String> map) {
        Integer page = Integer.parseInt(map.get("page"));
        return rankListService.ranklistpage(page);
    }

    @GetMapping("/user/ranklist/count")
    public Integer count() {
        return rankListService.count();
    }
}
