package com.cjy.domain.vo;

import com.cjy.domain.po.Prompt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromptVO {
    private Long id; // id
    private String promptName; // 提示词角色名称
    private String content; // 提示词内容
    private String modelType; // 模型类型
    private Boolean status; // 状态 1启用 0禁用

    //po转vo
    public static PromptVO of(Prompt prompt){
        PromptVO promptVo = new PromptVO();
        BeanUtils.copyProperties(prompt, promptVo);
        return promptVo;
    }
}
