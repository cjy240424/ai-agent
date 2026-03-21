package com.cjy.service;

import com.cjy.record.MessageVO;

import java.util.List;
import java.util.Set;

public interface ChatHistoryService {

    // 1. 记录用户的会话 ID
    void recordChatSession(String userId, String chatId);

    // 2. 获取用户的所有会话 ID
    Set<String> getUserChatSessions(String userId);

    // 3. ⚠️ 新增：获取单个会话的干净聊天记录
    List<MessageVO> getChatHistory(String chatId);
}