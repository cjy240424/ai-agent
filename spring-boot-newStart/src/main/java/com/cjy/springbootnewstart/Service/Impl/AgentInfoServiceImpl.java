package com.cjy.springbootnewstart.Service.Impl;

import com.cjy.springbootnewstart.Service.AgentInfoService;
import com.cjy.springbootnewstart.mapper.AgentInfoMapper;
import com.cjy.springbootnewstart.pojo.AgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentInfoServiceImpl implements AgentInfoService {

    @Autowired
    private AgentInfoMapper agentInfoMapper;

    public AgentInfo findAgent(Integer id) {
        System.out.println("【Service 层】查询单个机器人 ID=" + id);
        return agentInfoMapper.getAgentById(id);
    }

    public java.util.List<AgentInfo> findAllAgents() {
        System.out.println("【Service 层】查询所有机器人");
        return agentInfoMapper.findAllAgents();
    }

    public boolean insertAgent(AgentInfo agentInfo) {
        System.out.println("【Service 层】新增机器人：" + agentInfo.getName());
        int rows = agentInfoMapper.insertAgent(agentInfo);
        return rows > 0;
    }

    public boolean updateAgent(AgentInfo agentInfo) {
        System.out.println("【Service 句】更新机器人 ID=" + agentInfo.getId());
        int rows = agentInfoMapper.updateAgent(agentInfo);
        return rows > 0;
    }

    public boolean deleteAgent(Integer id) {
        System.out.println("【Service 层】删除机器人 ID=" + id);
        int rows = agentInfoMapper.deleteAgent(id);
        return rows > 0;
    }
}
