package com.cjy.controller;

import com.cjy.service.ChatHistoryService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ChatHistoryService chatHistoryService;

    @RequestMapping(value = "/chat/{userId}", produces = "text/html;charset=utf-8")
    public Flux<String> chat(
            @PathVariable("userId") String userId,
            @RequestParam("chatId") String chatId, // ⚠️ 极其关键：新增 chatId 参数！
            @RequestParam("prompt") String prompt){

        // 💎 联动核心：大模型思考前，先把这个会话归档到该用户的小本本上！
        chatHistoryService.recordChatSession(userId, chatId);

        return chatClient.prompt()
                .user(prompt)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory)
                        .conversationId(chatId) // ⚠️ 强行锁死：这里填的必须是 chatId，不能是 userId 啦！
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
