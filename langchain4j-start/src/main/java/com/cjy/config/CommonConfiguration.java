package com.cjy.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {

    /**
     * 为 ReviewRiskAgent 提供 ChatMemory 实例
     */
    @Bean
    public ChatMemory reviewRiskChatMemory() {
        // 使用消息窗口记忆，最多保留最近 10 条消息
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
