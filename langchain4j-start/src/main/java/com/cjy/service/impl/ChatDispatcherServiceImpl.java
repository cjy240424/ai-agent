package com.cjy.service.impl;

import com.cjy.agent.RouterAgent;
import com.cjy.record.IntentType;
import com.cjy.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatDispatcherServiceImpl implements ChatDispatcherService {

    private final RouterAgent routerAgent;
    private final LogisticsService logisticsService;
    private final RefundService refundService;
    private final PolicyService policyService;
    private final UnknownService unknownService;
    @Override
    public String processUserMessage(String userId, String userMessage) {
        IntentType intentType = routerAgent.routeIntent(userMessage);
        System.out.println("====== [路由追踪] 当前命中意图: " + intentType + " ======");

        String answer;
        // 1. 根据意图类型，调用对应的业务代理，逻辑先写下来，逻辑中只调用service
        //2. 创建各agent的service层，service调用各专业agent
        //3. 现在有milvus数据库了，三个专业ai直接查库，可以在tool中写调用库的方法。
        //....剩下的你来想吧
        switch (intentType) {
            case LOGISTICS:
                answer = logisticsService.processUserMessage(userId, userMessage);
                break;
            case REFUND:
                answer = refundService.processUserMessage(userId, userMessage);
                break;
            case POLICY:
                answer = policyService.processUserMessage(userId, userMessage);
                break;
            case UNKNOWN:
                answer = unknownService.processUserMessage(userId, userMessage);
                break;
            default:
                return "请表达清楚您的意图，我会认真处理的！";
        }
        return "[路由追踪: " + intentType.name() + "] -> " + answer;
    }
}
