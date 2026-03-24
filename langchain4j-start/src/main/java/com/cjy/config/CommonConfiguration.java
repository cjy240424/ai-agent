package com.cjy.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    /**
     * 为 ReviewRiskAgent 提供 ChatMemory 实例
     * 注意：ChatLanguageModel 由 langchain4j-open-ai-spring-boot-starter 自动配置
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        // 使用消息窗口记忆，最多保留最近 10 条消息
        return memoryId -> MessageWindowChatMemory.withMaxMessages(20);
    }
}
