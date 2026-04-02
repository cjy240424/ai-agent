package com.cjy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjy.pojo.AgentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AgentInfoMapper extends BaseMapper<AgentInfo> {
//
//    @Select("select id, name, status from agent_db.agent_info where id = #{id} ")
//    AgentInfo agentSearchById(Integer id);
}
