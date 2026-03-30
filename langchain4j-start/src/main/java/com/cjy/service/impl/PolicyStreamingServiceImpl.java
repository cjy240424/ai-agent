package com.cjy.service.impl;

import com.cjy.agent.PolicyStreamingAgent;
import com.cjy.service.PolicyStreamingService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyStreamingServiceImpl implements PolicyStreamingService {

    // 注入基础单例组件
    private final StreamingChatLanguageModel thinkingStreamingChatModel;
    private final EmbeddingStore<TextSegment> milvusEmbeddingStore;
    private final QwenEmbeddingModel dashScopeEmbeddingModel;
    private final ChatMemoryProvider chatMemoryProvider;

    @Override
    public TokenStream getDynamicPolicyStream(String userId, String question, String category) {
        // 1. 显式构建拦截漏斗（不再用 ThreadLocal 这种旁门左道）
        Filter filter = null;
        if (category != null && !category.trim().isEmpty()) {
            filter = MetadataFilterBuilder.metadataKey("category").isEqualTo(category);
        }

        // 2. 组装带专属漏斗的检索器
        ContentRetriever dynamicRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(milvusEmbeddingStore)
                .embeddingModel(dashScopeEmbeddingModel)
                .maxResults(2)
                .minScore(0.7)
                .filter(filter)
                .build();

        // 3. 动态组装 Agent
        PolicyStreamingAgent policyStreamingAgent = AiServices.builder(PolicyStreamingAgent.class)
                .streamingChatLanguageModel(thinkingStreamingChatModel)
                .retrievalAugmentor(DefaultRetrievalAugmentor.builder().contentRetriever(dynamicRetriever).build())
                .chatMemoryProvider(chatMemoryProvider)
                .build();

        // 4. 返回流
        return policyStreamingAgent.anwerser(userId, question);
    }
}

