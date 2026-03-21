package com.cjy.controller;

import com.cjy.record.Result;
import com.cjy.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/ai/history")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    @GetMapping("/{userId}/list")
    public Result<Set<String>> list(@PathVariable("userId") String userId) {
        // 从缓存中获取该用户的所有 chatId
        Set<String> sessions = chatHistoryService.getUserChatSessions(userId);
        // 包装成统一结构返回
        return Result.success(sessions);
    }
}