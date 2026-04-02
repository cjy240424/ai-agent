package com.cjy.controller;


import com.cjy.pojo.AgentInfo;
import com.cjy.pojo.Result;
import com.cjy.service.AgentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lly")
@RequiredArgsConstructor
public class AgentInfoController {

    //注入 Service
    private final AgentInfoService agentInfoService;
    @GetMapping("/cjy")
    public Result agentSearchById(Integer id){
        //搜索成功
        AgentInfo agent = agentInfoService.agentSearchById(id);
        if(agent != null){
            return Result.success(agent);
        }
        //搜索不到，失败
        return Result.error("搜索不到");
    }

    @PutMapping("/cjy2/{id}")
    public Result agentUpdate( @PathVariable("id") Integer id, @RequestParam String name){
        agentInfoService.lambdaUpdate()
                .eq(AgentInfo::getId, id)
                .set(AgentInfo::getName, name)
                .update();

        return Result.success();
    }
}
