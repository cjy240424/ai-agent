package com.cjy.controller;

import com.cjy.record.R;
import com.cjy.agent.RefundAgent;
import com.cjy.tools.ResearchToolAndModelUse;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class RefundController {

    private final RefundAgent refundAgent;

    @GetMapping("/{userId}/refund")
    public R<String> refund(
            @PathVariable("userId") String userId,
            @RequestParam("userMessage") String userMessage
    ){
        log.info("拿到的 userId 是: " + userId);

        Result<String> result = refundAgent.refund(userId, userMessage);

        ResearchToolAndModelUse.research(result);

        return R.success(result.content());

    }
}
