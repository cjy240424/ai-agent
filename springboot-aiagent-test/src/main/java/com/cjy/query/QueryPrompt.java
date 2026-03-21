package com.cjy.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryPrompt extends PageQuery{
    private Long id; // id
    private String promptName; // 提示词角色名称
    private String content; // 提示词内容
    private String modelType; // 模型类型
    private Boolean status; // 状态 1启用 0禁用
}
