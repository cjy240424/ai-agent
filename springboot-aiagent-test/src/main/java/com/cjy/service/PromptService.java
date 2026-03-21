package com.cjy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjy.domain.dto.PageDTO;
import com.cjy.domain.dto.PromptCreatDTO;
import com.cjy.domain.dto.PromptUpdateDTO;
import com.cjy.domain.po.Prompt;
import com.cjy.domain.vo.PromptVO;
import com.cjy.query.QueryPrompt;

import java.util.List;

public interface PromptService extends IService<Prompt> {

   //查询名字是否存在
   public boolean isNameExist(String promptName);

   //根据复杂条件查询查询List<prompt>
    public List<Prompt> QueryPrompts(Long id,
                                     String promptName,
                                     String content,
                                     String modelType,
                                     Boolean status);

   //添加prompt
   public boolean addPrompt(PromptCreatDTO promptCreatDto);

   //根据id查找prompt
   public PromptVO getPromptById(Long id);

   //分页查询
   PageDTO<PromptVO> queryPromptPage(QueryPrompt queryPrompt);

   //根据id修改PromptName和content
   Boolean updatePromptById(PromptUpdateDTO promptUpdateDto);
}
