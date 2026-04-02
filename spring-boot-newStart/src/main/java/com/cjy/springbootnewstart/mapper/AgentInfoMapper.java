package com.cjy.springbootnewstart.mapper;

import com.cjy.springbootnewstart.pojo.AgentInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AgentInfoMapper {

    @Select("select * from agent_info where id = #{id}")
    AgentInfo getAgentById(Integer id);

    @Select("select * from agent_info")
    List<AgentInfo> findAllAgents();

    @Insert("insert into agent_info(name, description) values(#{name}, #{description})")
    int insertAgent(AgentInfo agentInfo);

    @Update("update agent_info set name=#{name}, description=#{description} where id=#{id}")
    int updateAgent(AgentInfo agentInfo);

    @Delete("delete from agent_info where id = #{id}")
    int deleteAgent(Integer id);
}
