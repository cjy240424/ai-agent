package com.cjy.service.Impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjy.domain.dto.PageDTO;
import com.cjy.domain.dto.PromptCreatDTO;
import com.cjy.domain.dto.PromptUpdateDTO;
import com.cjy.domain.po.Prompt;
import com.cjy.domain.vo.PromptVO;
import com.cjy.mapper.PromptMapper;
import com.cjy.query.QueryPrompt;
import com.cjy.service.PromptService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PromptServiceImpl  extends ServiceImpl<PromptMapper, Prompt> implements PromptService {
    @Override
    public boolean isNameExist(String promptName) {
        //1. 查询提示词角色名称是否已存在
        if (lambdaQuery()
                .eq(Prompt::getPromptName, promptName)
                .exists()) {
            return true;
        }
        return false;
    }

    @Override
    public List<Prompt> QueryPrompts(Long id, String promptName, String content, String modelType, Boolean status) {
        return lambdaQuery()
                .eq(id != null, Prompt::getId, id)
                // 只有当 promptName 不为 null 且不是空字符串时，才添加模糊查询条件
                .like(StringUtils.isNotBlank(promptName), Prompt::getPromptName, promptName)
                // 其他条件同理
                .like(StringUtils.isNotBlank(content), Prompt::getContent, content)
                .like(StringUtils.isNotBlank(modelType), Prompt::getModelType, modelType)
                .eq(status != null, Prompt::getStatus, status)
                .list();
    }

    @Override
    public boolean addPrompt(PromptCreatDTO promptCreatDto) {

        //2. 添加提示词数据
        //2.1 创建po对象
        Prompt prompt = PromptCreatDTO.toPo(promptCreatDto);
        //2.2 设置剩余字段值
        prompt.setStatus(true);
        prompt.setCreateTime(LocalDateTime.now());
        prompt.setUpdateTime(LocalDateTime.now());
        //2.3 保存数据
        if (save(prompt)) {
            return true;
        }
        return false;
    }

    @Override
    public PromptVO getPromptById(Long id) {
        return  Optional.ofNullable(lambdaQuery()
                .eq(Prompt::getId, id)
                .one())
                .map(PromptVO::of)
                .orElse( null); //负责解包，否则还是Optional对象（封装<PromptVO> ）
                                        //Optional<PromptVO>
    }

    @Override
    public PageDTO<PromptVO> queryPromptPage(QueryPrompt queryPrompt) {
        //1. 创建分页、排序条件
        Page<Prompt> page = queryPrompt.toMpPage(queryPrompt.getIsAsc(), queryPrompt.getSortBy());
        //2. 分页查询
        Page<Prompt> p = lambdaQuery()
                .like(StringUtils.isNotBlank(queryPrompt.getPromptName()), Prompt::getPromptName, queryPrompt.getPromptName())
                .eq(queryPrompt.getStatus() != null, Prompt::getStatus, queryPrompt.getStatus())
                .like(StringUtils.isNotBlank(queryPrompt.getModelType()), Prompt::getModelType, queryPrompt.getModelType())
                .eq(queryPrompt.getId() != null, Prompt::getId, queryPrompt.getId())
                .page(page);
        //3. 获取结果
        return PageDTO.of(p, PromptVO.class);
//        //3. 封装结果
//        PageDTO<PromptVO> pageDto = new PageDTO<>();
//        //3.1 设置总结果数
//        pageDto.setTotal(page.getTotal());
//        //3.2 设置总页数
//        pageDto.setPages(page.getPages());
//        //3.3 封装结果集合
//        List<PromptVO> list = page.getRecords().stream()
//                .map(PromptVO::of)
//                .toList();
//        //3.4 判断结果集合是否为空
//        if (CollectionUtils.isEmpty(list)){
//            pageDto.setList(Collections.emptyList());
//            return pageDto;
//        }
//        pageDto.setList(list);
//        return pageDto;
    }

    @Override
    public Boolean updatePromptById(PromptUpdateDTO promptUpdateDto) {
        //1. 转型
        Prompt prompt = PromptUpdateDTO.toPo(promptUpdateDto);
        //2. 修改
        return lambdaUpdate()
                .eq(Prompt::getId, promptUpdateDto.getId())
                .update(prompt);
    }
}