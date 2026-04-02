package com.cjy.tools;


import dev.langchain4j.service.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResearchToolAndModelUse {


    public static <T> void research(Result<T> result) {
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
        log.info("消耗的总 Token 数: " + result.tokenUsage().totalTokenCount());
    }
}
