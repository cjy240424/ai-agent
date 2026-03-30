package com.cjy.service.impl;

import com.cjy.agent.ChatAgent;
import com.cjy.service.UnknownService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnknownServiceImpl implements UnknownService {

    private final ChatAgent chatAgent;
    @Override
    public String processUserMessage(String userId, String userMessage) {
        return chatAgent.chat(userId, userMessage).content();
    }
}
