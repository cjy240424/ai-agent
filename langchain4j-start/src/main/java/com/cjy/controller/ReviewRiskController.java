package com.cjy.controller;

import com.cjy.record.R;
import com.cjy.record.ReviewAnalysis;
import com.cjy.service.ReviewRiskAgent;
import dev.langchain4j.service.Result;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/agent")
public class ReviewRiskController {

    private final ReviewRiskAgent agent;

    @GetMapping("/{userId}/review")
    public R<ReviewAnalysis> analyzeReview(
            @PathVariable String userId,
            @RequestParam("reviewText") String reviewText
    ) {
        System.out.println("拿到的 userId 是: " + userId); // 👈 加上这句
 //-----------------------------------------------
        //调用接口，底层的代理对象会自动构建 Prompt、调用大模型、并反序列化 JSON
        Result<ReviewAnalysis> result = agent.analyzeReview(userId, reviewText);

        // 获取强类型映射结果
        ReviewAnalysis reviewAnalysis = result.content();
        // 6. 获取调用背后的元数据
        System.out.println("消耗的总 Token 数: " + result.tokenUsage().totalTokenCount());

        return R.success(reviewAnalysis);
    }
}
