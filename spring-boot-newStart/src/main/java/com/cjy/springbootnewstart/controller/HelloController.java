package com.cjy.springbootnewstart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "今天看到了李林炎的侧颜，还是那么好看！我不是猥琐，我只是在留恋";
    }
}
