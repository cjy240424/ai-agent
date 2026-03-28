package com.cjy.controller;

import com.cjy.record.R;
import com.cjy.service.PolicyAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyAgent policyAgent;

    @GetMapping("/{userId}/ask")
    public R<String> ask(
            @PathVariable String userId,
            @RequestParam String question){


        return R.success(policyAgent.anwerser(userId, question).content());
    }
}
