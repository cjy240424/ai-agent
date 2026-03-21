package com.cjy.controller;

import com.cjy.record.MessageVO;
import com.cjy.record.Result;
import com.cjy.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor // ⚠️ 别忘了加，用来注入 Service
public class ChatDetailsController {

    private final ChatHistoryService chatHistoryService;

    // 完美保留你设计的路径层级
    @GetMapping("/{userId}/{chatId}")
    public Result<List<MessageVO>> getChatDetails(
            @PathVariable("userId") String userId,
            @PathVariable("chatId") String chatId
    ) {
        // 获取该会话清洗后的详细内容，并包上 Result 壳子返回
        return Result.success(chatHistoryService.getChatHistory(chatId));
    }
}