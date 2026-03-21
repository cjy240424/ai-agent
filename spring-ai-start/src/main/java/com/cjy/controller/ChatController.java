package com.cjy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

    @RequiredArgsConstructor
    @RestController
    @RequestMapping("api/v1/ai")
    public class ChatController {

        private final ChatClient chatClient;
        private final ChatMemory chatMemory;

        @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
        public Flux<String> chat(// ⚠️ 极其关键：必须加上 @RequestParam 强绑定参数，防 null 杀手！
                                 @RequestParam("userId") String userId,
                                 @RequestParam("prompt") String prompt){
            return chatClient.prompt()
                    .user(prompt)
                    .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                            .conversationId(userId) // 强行锁死当前用户的专属 ID
                            .build())
                    .stream()
                    .content();
        }

        @RequestMapping(value = "/ask", produces = "text/html;charset=utf-8")
        public Flux<String> chat(String prompt, Boolean isVIP){
            if (isVIP != null && isVIP) {
                return chatClient.prompt()
                        .system("你现在是一个年薪百万的顶级Java架构师，请用极其严谨、专业、惜字如金的语气回答问题，绝对不要卖萌。") //动态覆盖system
                        .user(prompt)
                        .options(OpenAiChatOptions.builder()
                                .model("deepseek-r1") // 动态覆盖 yml 里的模型名称
                                .temperature(0.8)  // VIP 专享：让回答更有创造力
                                .build())
                        .stream()
                        .content();
            }
            return chatClient.prompt()
                    .user(prompt)
                    .stream()
                    .content();
        }
    }
