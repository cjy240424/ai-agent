package com.cjy.agent;

import com.cjy.record.ReviewAnalysis;
import dev.langchain4j.service.*;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface ReviewRiskAgent {

    @SystemMessage({
            "你是阿里巴巴资深的电商风控专家。",
            "你的任务是精准分析用户的商品评价，并严格提取关键信息。",
            "【强制输出字段与业务规则说明】",
            "1. sentiment (String): 情感倾向，取值必须且只能是以下三个单词之一：'POSITIVE', 'NEGATIVE', 'NEUTRAL'。",
            "2. keywords (List<String>): 提取出的核心痛点关键词，数量必须限制在 3 个以内。",
            "3. dangerScore (int): 危险评分，范围必须在 0 - 100 之间。如果是辱骂、投诉、威胁等极端言论，必须打高分（80-100）。",
            "4. summary (String): 用极其精简的一句话总结这条评论的核心诉求或情绪。",
            "",
            "【严格格式要求】",
            "你的输出必须是一个纯净的 JSON 对象，直接返回 JSON 即可，绝对不允许包含任何 Markdown 标记（如 ```json）、反引号或任何额外的废话解释。"
    })
    @UserMessage("请分析以下用户评论：\n\n --- \n {{reviewText}} \n ---")
    Result<ReviewAnalysis> analyzeReview(
            @MemoryId String userId,
            @V("reviewText") String reviewText); // 注意：这里直接返回 Result<Record> 对象！
}