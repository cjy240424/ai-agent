package com.cjy.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.MemoryId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LogisticsTools {

    @Bean
    // 核心难点：这里的描述决定了大模型会不会调、懂不懂传什么参数！
    @Tool("根据订单号查询最新的物流状态轨迹。订单号通常是以 'ORD-' 开头的字符串。")
    public String getLogisticsInfo(
            @MemoryId String userId,
            @P("订单编号") String orderId) {
        // 强制要求：在这里加上一行打印日志，用来证明大模型真的通过反射调用了你的方法！
        System.out.println("🔧 [底层工具被触发] 正在查询数据库，用户: " + userId + "，订单: " + orderId);
        // 模拟数据库查询
        if ("ORD-8899".equals(orderId) && "cjy".equals(userId)) {
            return "您的包裹已到达【北京市朝阳区三里屯集散中心】，派件人：孙悟空（电话13800000000），预计今天下午18:00前送达。";
        }
        return "抱歉，数据库中未查到该订单号的物流信息。";
    }
}
