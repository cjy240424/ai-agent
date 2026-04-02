package com.cjy.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjy.mapper.AgentInfoMapper;
import com.cjy.pojo.AgentInfo;
import com.cjy.service.AgentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentInfoServiceImpl extends ServiceImpl<AgentInfoMapper, AgentInfo> implements AgentInfoService {

    @Autowired
    private AgentInfoMapper agentInfoMapper;
    @Override
    public AgentInfo agentSearchById(Integer id) {
        AgentInfo agent = agentInfoMapper.selectById(id);
        return agent;
    }
}
