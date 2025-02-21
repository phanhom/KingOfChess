package com.phanhom.kob.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pk")
public class pkController {
    @RequestMapping("")
    public String hello() {
        return "Hello, World!";
    }
}
