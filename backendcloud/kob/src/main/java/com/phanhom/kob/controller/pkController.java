package com.phanhom.kob.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/pk")
public class pkController {
    @RequestMapping("")
    public String hello() {
        return "Fighting!!!!!!";
    }
}
