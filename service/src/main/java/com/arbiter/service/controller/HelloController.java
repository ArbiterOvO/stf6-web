package com.arbiter.service.controller;

import com.arbiter.common.result.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping()
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
