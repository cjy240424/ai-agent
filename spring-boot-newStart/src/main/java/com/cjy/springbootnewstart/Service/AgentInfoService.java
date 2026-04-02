package com.cjy.springbootnewstart.Service;

import com.cjy.springbootnewstart.pojo.AgentInfo;

import java.util.List;

public interface AgentInfoService {
    // 根据 id 查询
    AgentInfo findAgent(Integer id);

    // 查询所有
    List<AgentInfo> findAllAgents();

    // 新增
    boolean insertAgent(AgentInfo agentInfo);

    // 更新
    boolean updateAgent(AgentInfo agentInfo);

    // 删除
    boolean deleteAgent(Integer id);
}
