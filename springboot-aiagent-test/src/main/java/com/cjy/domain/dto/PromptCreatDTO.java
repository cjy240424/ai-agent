package com.cjy.domain.dto;

import com.cjy.domain.po.Prompt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromptCreatDTO {
    private String promptName; // 提示词角色名称
    private String content; // 提示词内容
    private String modelType; // 模型类型

    public static Prompt toPo(PromptCreatDTO pcDto){
        Prompt prompt = new Prompt();
        BeanUtils.copyProperties(pcDto, prompt);
        return prompt;
    }
}
