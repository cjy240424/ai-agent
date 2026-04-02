package com.cjy.springbootnewstart.controller;

import com.cjy.springbootnewstart.Service.AgentInfoService;
import com.cjy.springbootnewstart.pojo.AgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController // 表明这是专门接待网络请求，并返回 JSON 的前台
public class AgentInfoController {

    @Qualifier("agentInfoServiceImpl")
    @Autowired // Spring自动把上面那个 Service 大脑请过来给你派活儿
    private AgentInfoService agentInfoService;

    // 规定好：只要浏览器访问 /agent/1，这个 1 就会变成 id 传进方法里
    @RequestMapping("/agent/{id}")
    public AgentInfo getAgent(@PathVariable Integer id) {
        System.out.println("【Controller层】收到前端请求，要查询的ID是：" + id);

        // Controller自己不干活，直接把 id 扔给 Service 大脑去处理
        AgentInfo finalResult = agentInfoService.findAgent(id);

        // 把结果扔回去给前端，Spring Boot 会自动把它变成 JSON！
        return finalResult;
    }
}