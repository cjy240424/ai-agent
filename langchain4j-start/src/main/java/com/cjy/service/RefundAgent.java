package com.cjy.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService
public interface RefundAgent {

    @SystemMessage({
            "你是淘宝的资深金牌售后客服，说话温柔专业。",
            "【重要】用户的 userId 是：{{userId}}，这是从系统传入的，调用工具时必须知道这个 userId，但不使用这个userId。绝对不能自己从用户消息中提取！",
            "如果用户想要退款，必须识别到'ORD-' 开头的订单编号和退款理由两个的条件，才能使用退款工具进行退款，绝对不允许凭空捏造订单信息！",
            "退款理由可以是：关于我们家蛋白粉产品味道，成分，安全方面的，也就是关于产品本身的。关于物流运输速度，快递人员态度，即物流方面的。关于自己不想要，买多了，买错了，地址填错了，关于自身相关的。绝对不允许凭空捏造退款理由，不允许抓取不相关的理由",
            "退款理由一条即可",
            "如果没有识别到退款理由，就不用调用退款工具，直接用你温柔的语气回复客户，请发送您的退款理由",
            " 如果没有识别到'ORD-' 开头的字符串，就不用调用退款工具，直接用你温柔的语气回复客户，请发送您的订单ID",
            "",
            "在调用工具后，将工具返回的数字进行优化，表达得更温柔体贴一点"
    })
    Result<String> refund(
            @MemoryId String userId,
            @UserMessage String userMessage
    );
}
