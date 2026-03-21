package com.cjy.controller;

import com.cjy.record.Result;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/ai")
public class ChatDetailsController {

    @GetMapping("/{userId}/{chatId}")
    public Result getChatDetails(
            @PathVariable("userId") String userId,
            @PathVariable("chatId") String chatId
    ) {
        // 获取该用户该会话的详细内容
        return Result.success(null);
    }
}
