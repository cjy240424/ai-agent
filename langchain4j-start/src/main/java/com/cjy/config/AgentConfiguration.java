package com.cjy.config;

import com.cjy.agent.*;
import com.cjy.tools.OrderTools;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfiguration {
    /**
     * 1. 组装路由专家 (RouterAgent)
     * 特点：不需要记忆，不需要工具，不需要知识库。快进快出！
     */
    @Bean
    public RouterAgent routerAgent(ChatLanguageModel chatModel) {
        return AiServices.builder(RouterAgent.class)
                .chatLanguageModel(chatModel)
                .build();
    }

    /**
     * 2. 组装物流专家 (LogisticsAgent)
     * 特点：【策略一生效】只给它配发 OrderTools！绝对不给 RAG！
     */
    @Bean
    public LogisticsAgent logisticsAgent(ChatLanguageModel chatModel, OrderTools orderTools) {
        return AiServices.builder(LogisticsAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10)) // 开启记忆
                .tools(orderTools) // 🔫 物理隔离：只注入物流相关的 Tool！
                .build();
    }

    /**
     * 3. 组装规章专家 (PolicyAgent)
     * 特点：【策略二生效】只给它配发 Milvus 检索器！绝对不给 Tool！
     */
    @Bean
    public PolicyAgent policyAgent(
            ChatLanguageModel chatModel,
            EmbeddingModel qwenEmbeddingModel,            // 1. 注入向量化模型
            EmbeddingStore<TextSegment> milvusEmbeddingStore // 2. 注入 Milvus 连接
    ) {

        // 【核心绝招：私有化锻造武器】
        // 在这里直接 new 这个 Retriever，不加 @Bean！
        // 这样它就彻底变成了 PolicyAgent 的私有财产，绝对不可能污染其他 Agent！
//        ContentRetriever privateMilvusRetriever = EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(milvusEmbeddingStore)
//                .embeddingModel(qwenEmbeddingModel)
//                .maxResults(2)
//                .minScore(0.6)
//                .build();
        return AiServices.builder(PolicyAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.builder() //ContentRetriever建造对象
                        .embeddingStore(milvusEmbeddingStore)
                        .embeddingModel(qwenEmbeddingModel)
                        .maxResults(2)
                        .minScore(0.6)
                        .build()) // 📚 物理隔离：只注入知识库！
                .build();
    }

    /**
     * 4. 组装兜底闲聊专家 (ChatAgent / UnknownAgent)
     * 特点：什么都不给，只给一个脑子和记忆，负责礼貌拒绝。
     */
    @Bean
    public ChatAgent chatAgent(ChatLanguageModel chatModel) {
        return AiServices.builder(ChatAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .build();
    }

    /**
     * 退款专家 (RefundAgent)
     * 特点：【策略三生效】只给它配发 Tool！绝对不给 Milvus 检索器！
     */
    @Bean
    public RefundAgent refundAgent(ChatLanguageModel chatModel, OrderTools orderTools) {
        return AiServices.builder(RefundAgent.class)
                .chatLanguageModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(orderTools)
                .build();
    }
}
