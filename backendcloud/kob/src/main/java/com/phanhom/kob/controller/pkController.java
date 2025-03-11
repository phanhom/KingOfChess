package com.phanhom.kob.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pk")
@CrossOrigin(origins = "http://localhost:5173") // 允许的前端域名
public class pkController {
    @RequestMapping("")
    public String hello() {
        return "Fighting!!!!!!";
    }
}
