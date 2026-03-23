package com.cjy.controller;

import com.cjy.record.R;
import com.cjy.service.LogisticsAgent;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsAgent logisticsAgent;
    @GetMapping("/logistics/{userId}")
    public R<String> orderQuery(
            @PathVariable String userId,
            @RequestParam("userMessage") String userMessage
    ) {
        log.info("拿到的 userId 是: " + userId); // 👈 添加这句
        // 1. 调用接口，底层的代理对象会自动构建 Prompt、调用大模型、并反序列化 JSON
        Result<String> result = logisticsAgent.chat(userId, userMessage);

        // 2. 🛡️ 大厂必备：打印完整审计日志。同时监控“工具请求参数”与“工具底层真实返回值”
        if (result.toolExecutions() != null && !result.toolExecutions().isEmpty()) {
            result.toolExecutions().forEach(execution -> {
                // 1. 获取大模型发出的“施工单” (上半场)
                String toolName = execution.request().name();
                String toolArgs = execution.request().arguments();

                // 2. 获取底层 Java 方法真实查出的“数据结果” (下半场 - 新版 API 的杀手锏)
                String toolResult = execution.result();

                log.info("🤖 [Agent审计] 触发工具: {} | 提取参数: {} | 底层真实返回: {}",
                        toolName, toolArgs, toolResult);
            });
        }

        // 3. 获取调用背后的元数据
        System.out.println("消耗的总 Token 数: " + result.tokenUsage().totalTokenCount());

        return R.success(result.content());

        // toolResult vs result.content()
        /*
        🔪 物理本质解剖：原材料 vs 最终成品
        1. toolResult (execution.result()) = 刚挖出来的原材料
        它是谁产生的：它是你的 Java 后端代码（@Tool 方法） 产生的。

        它长什么样：它通常是干巴巴的数据、JSON，或者是你在 Java 里直接 return 的那串原始字符串。

        物理意义：这是 Java 查完数据库后，秘密汇报给大模型的证据（也就是我们说的“第二趟网络请求”的入参）。

        举个例子："STATUS_SHIPPED, SF12345678"

        2. result.content() = 大模型做好的精美菜肴
        它是谁产生的：它是 大模型（比如 Qwen3） 经过思考和润色后产生的。

        它长什么样：它是一句完美的、符合 @SystemMessage 语气设定的人类自然语言。

        物理意义：这是大模型拿到了 toolResult（原材料）之后，经过组织语言，最终准备发给用户前端的正式回答。

        举个例子："亲爱的曹总，帮您查到啦！您的订单目前已经发货，顺丰快递单号是 SF12345678，请注意查收哦~"
         */
    }
}
