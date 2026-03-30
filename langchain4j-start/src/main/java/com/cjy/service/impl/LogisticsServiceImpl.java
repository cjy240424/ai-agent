package com.cjy.service.impl;

import com.cjy.agent.LogisticsAgent;
import com.cjy.service.LogisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsAgent logisticsAgent;

    @Override
    public String processUserMessage(String userId, String userMessage) {
        return logisticsAgent.chat(userId, userMessage).content();
    }
}
