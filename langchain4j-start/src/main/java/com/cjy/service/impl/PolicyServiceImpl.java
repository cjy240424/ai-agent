package com.cjy.service.impl;

import com.cjy.agent.PolicyAgent;
import com.cjy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyAgent policyAgent;
    @Override
    public String processUserMessage(String userId, String userMessage) {
        return policyAgent.anwerser(userId, userMessage).content();
    }
}
