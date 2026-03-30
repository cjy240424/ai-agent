package com.cjy.service.impl;

import com.cjy.agent.RefundAgent;
import com.cjy.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundAgent refundAgent;
    @Override
    public String processUserMessage(String userId, String userMessage) {
        return refundAgent.refund(userId, userMessage).content();
    }
}
