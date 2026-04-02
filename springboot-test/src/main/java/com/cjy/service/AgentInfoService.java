package com.cjy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjy.pojo.AgentInfo;

public interface AgentInfoService extends IService<AgentInfo> {
    AgentInfo agentSearchById(Integer id);
}
