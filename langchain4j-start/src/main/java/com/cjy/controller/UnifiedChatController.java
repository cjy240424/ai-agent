package com.cjy.controller;

import com.cjy.record.R;
import com.cjy.service.ChatDispatcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class UnifiedChatController {

    private final ChatDispatcherService chatDispatcherService;

    @GetMapping("/dispatcher/{userId}")
    public R<String> unifiedChat(
           @PathVariable String userId,
           @RequestParam("message") String userMessage
    ){
        return R.success(chatDispatcherService.processUserMessage(userId, userMessage));
    }
}
