package com.cjy.record;

import java.util.List;

// 这是你的下游业务真正需要的数据结构
public record ZhuanBaoResponse(
        String originalMsg,    // 用户原始输入的白话
        String translatedMsg,  // 翻译出的大厂黑话
        List<String> keyTokens // 提取的核心黑话词汇（比如：赋能、闭环）
) {}