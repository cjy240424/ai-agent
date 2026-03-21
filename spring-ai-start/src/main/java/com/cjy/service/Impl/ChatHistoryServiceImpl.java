package com.cjy.service.Impl;

import com.cjy.record.MessageVO;
import com.cjy.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@RequiredArgsConstructor
@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    /**
     * 👑 核心缓存结构：UserId -> Set<ChatId>
     * 为什么用 CopyOnWriteArraySet？
     * 1. Set 天生去重：如果同一个 chatId 传了两次，不会重复保存。
     * 2. CopyOnWrite：读多写少场景下的并发神器，迭代时绝对不会抛出 ConcurrentModificationException！
     */
    private final ConcurrentHashMap<String, CopyOnWriteArraySet<String>> userChatSessions = new ConcurrentHashMap<>();

    // ⚠️ 注入 Spring AI 全局唯一的物理记事本
    private final ChatMemory chatMemory;

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

    // 💎 核心大招：数据清洗流水线
    @Override
    public List<MessageVO> getChatHistory(String chatId) {
        // 1. 从底层捞出这个会话最近的 100 条“脏数据”
        List<Message> rawMessages = chatMemory.get(chatId);

        // 2. 准备一个干净的空箱子，准备装洗好的 VO
        List<MessageVO> cleanList = new ArrayList<>();

        // 3. 遍历底层数据，进行清洗
        for (Message msg : rawMessages) {
            // 架构师细节：系统人设（SystemPrompt）属于后端机密，绝对不能返回给前端！直接跳过！
            if (msg.getMessageType() == MessageType.SYSTEM) {
                continue;
            }

            // 判断这句话是谁说的
            String role = msg.getMessageType().getValue(); //== MessageType.USER ? "user" : "assistant";
            // 提取纯文本内容
            String content = msg.getText();

            // 4. 把洗干净的 role 和 content 实例化成 VO，扔进箱子里
            cleanList.add(new MessageVO(role, content));
        }

        // 5. 把装满 VO 的箱子返回
        return cleanList;
    }
}