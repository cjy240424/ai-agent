package com.cjy.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.MemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderTools {

    @Tool(name = "申请退款工具", value = "根据订单编号和退款理由申请退款。必须同时提供订单号和退款理由才能调用此工具")
    public String rufund(
            @P("订单编号") String orderId,
            @P("退款理由") String refundReason
    ){
        log.info("========== [高危操作] 触发退款本地方法! 单号: " + orderId + ", 理由: " + refundReason + " ==========");
        if ("ORD-8899".equals(orderId)) {
            return "退款接口调用成功，已向财务系统发起退款指令。";
        }
        return "退款失败，未找到该订单。";
    }



    // 核心难点：这里的描述决定了大模型会不会调、懂不懂传什么参数！
    @Tool(name = "查看订单物流信息工具", value = "根据订单号查询最新的物流状态轨迹。订单号通常是以 'ORD-' 开头的字符串。")
    public String getLogisticsInfo(
            @MemoryId String userId,
            @P("订单编号") String orderId) {
        // 强制要求：在这里加上一行打印日志，用来证明大模型真的通过反射调用了你的方法！
        log.info("🔧 [底层工具被触发] 正在查询数据库，用户: " + userId + "，订单: " + orderId);
        // 模拟数据库查询
        if ("ORD-8899".equals(orderId) && "cjy".equals(userId)) {
            return "您的包裹已到达【北京市朝阳区三里屯集散中心】，派件人：孙悟空（电话13800000000），预计今天下午18:00前送达。";
        }
        return "抱歉，数据库中未查到该订单号的物流信息。";
    }
}
