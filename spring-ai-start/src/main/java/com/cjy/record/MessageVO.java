package com.cjy.record;

// 极其干净的数据结构，前端拿到直接就能渲染出聊天气泡
public record MessageVO(
        String role,   // 角色：user 或者 assistant
        String content // 聊天的具体文本内容
) {}