package com.cjy.record;

import java.util.List;

public record ReviewAnalysis (
        String sentiment,      // 情感倾向 (只能是 POSITIVE, NEGATIVE, NEUTRAL 之一)
        List<String> keywords, // 提取出的3个核心痛点关键词
        int dangerScore,       // 危险评分 (0-100，如果是辱骂、退诉等极端言论，打高分)
        String summary         // 一句话总结这条评论
){}
