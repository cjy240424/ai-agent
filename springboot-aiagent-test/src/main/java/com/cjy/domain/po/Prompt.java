package com.cjy.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {
    private Long id; // id
    private String promptName; // 提示词角色名称
    private String content; // 提示词内容
    private String modelType; // 模型类型
    private Boolean status; // 状态 1启用 0禁用
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 修改时间
}
