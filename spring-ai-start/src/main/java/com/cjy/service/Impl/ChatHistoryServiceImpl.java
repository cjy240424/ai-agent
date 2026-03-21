package com.cjy.service.Impl;

import com.cjy.service.ChatHistoryService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    /**
     * 👑 核心缓存结构：UserId -> Set<ChatId>
     * 为什么用 CopyOnWriteArraySet？
     * 1. Set 天生去重：如果同一个 chatId 传了两次，不会重复保存。
     * 2. CopyOnWrite：读多写少场景下的并发神器，迭代时绝对不会抛出 ConcurrentModificationException！
     */
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<String>> userChatSessions = new ConcurrentHashMap<>();

    // 1. 记录用户的会话 ID
    public void recordChatSession(String userId, String chatId) {
        // computeIfAbsent：如果这个 userId 是第一次来，就帮他新建一个 Set；否则直接拿到老的 Set 并往里 add
        userChatSessions.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(chatId);
    }

    // 2. 获取用户的所有会话 ID
    public Set<String> getUserChatSessions(String userId) {
        // getOrDefault：如果用户没创建过任何会话，返回一个空的集合，防止前端拿到 null 报空指针
        return userChatSessions.getOrDefault(userId, new CopyOnWriteArraySet<>());
    }
}