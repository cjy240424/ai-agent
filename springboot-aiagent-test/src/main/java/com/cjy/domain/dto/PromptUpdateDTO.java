package com.cjy.domain.dto;

import com.cjy.domain.po.Prompt;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PromptUpdateDTO {
    private Long id; // id
    private String promptName; // 提示词角色名称
    private String content; // 提示词内容

    public static Prompt toPo(PromptUpdateDTO puDto){
        Prompt prompt = new Prompt();
        BeanUtils.copyProperties(puDto, prompt);
        return prompt;
    }
}
