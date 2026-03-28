package com.cjy.config;

import com.cjy.utils.CategoryContextHolder;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import dev.langchain4j.model.dashscope.QwenEmbeddingModel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Lazy;

@Configuration
public class CommonConfiguration {

    // ==================== 配置注入 ====================

    @Value("${my-milvus.uri}")
    private String milvusUri;

    @Value("${my-milvus.token}")
    private String milvusToken;

    @Value("${dashscope.api-key}")  // 👈 新增：从配置文件读取 API Key
    private String dashScopeApiKey;

    @Value("${dashscope.base-url}")
    private String dashScopeBaseUrl;

    // ==================== Bean 定义 ====================

    /**
     * ChatMemory 提供者 - 用于对话记忆
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(20);
    }

    /**
     * Milvus 向量存储 - 用于存储和检索向量
     * 添加 @Lazy 懒加载，避免启动时连接失败导致应用无法启动
     */
    @Bean
    public EmbeddingStore<TextSegment> milvusEmbeddingStore() {
        return MilvusEmbeddingStore.builder()
                .uri(milvusUri)
                .token(milvusToken)
                .collectionName("cjy_ecommerce_knowledge")
                .dimension(1536)  // 必须与 Embedding 模型输出维度一致
                .autoFlushOnInsert(true) // 👑 极其重要：强制数据立刻落盘，方便咱们去控制台“捉奸”！
                .build();
    }

    /**
     * DashScope Embedding 模型 - 用于文本向量化
     */
    @Bean
    public QwenEmbeddingModel dashScopeEmbeddingModel() {  // 👈 方法名建议小写开头
        QwenEmbeddingModel qwenEmbeddingModel = QwenEmbeddingModel.builder()
                .apiKey(dashScopeApiKey)      // 👈 从配置文件注入
                .modelName("text-embedding-v2") // 👈 推荐使用 v2 模型
                .build();
        return qwenEmbeddingModel;
    }

    /**
     * EmbeddingStoreIngestor - 用于向量灌库
     *
     * @param qwenEmbeddingModel
     * @param milvusEmbeddingStore
     * @return
     */

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(QwenEmbeddingModel qwenEmbeddingModel, EmbeddingStore<TextSegment> milvusEmbeddingStore) {
        return EmbeddingStoreIngestor.builder()
                .embeddingModel(qwenEmbeddingModel) // 使用上面定义的 qwenEmbeddingModel
                .embeddingStore(milvusEmbeddingStore) // 使用上面定义的 milvusEmbeddingStore
                // 这里未来还可以配置 DocumentSplitter (切肉机，专门把大 PDF 切成小片段)
                // 👑 极其核心的切片规则：调用 LangChain4j 官方提供的“递归智能切分器”
                .documentSplitter(DocumentSplitters.recursive(300, 50))
                .build();
    }

//    /**
//     * old version
//     *  ContentRetriever - 用于内容检索
//     * @param qwenEmbeddingModel
//     * @param milvusEmbeddingStore
//     * @return
//     */
//
//    @Bean
//    public ContentRetriever contentRetriever(
//            QwenEmbeddingModel qwenEmbeddingModel,
//            EmbeddingStore<TextSegment> milvusEmbeddingStore
//    ) {
//        return EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(milvusEmbeddingStore)   // 去哪里找？去 Milvus 里找
//                .embeddingModel(qwenEmbeddingModel) // 用什么模型把问题变成向量？
//                .maxResults(2)                  // 每次最多捞出 2 条最相关的机密文本
//                .minScore(0.6)                  // 相似度分数过滤，太低的不准不要
//                .build();
//    }

    @Bean
    public ContentRetriever contentRetriever(
            QwenEmbeddingModel dashScopeEmbeddingModel, // 注意名字和上面Bean保持一致
            EmbeddingStore<TextSegment> milvusEmbeddingStore
    ) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(milvusEmbeddingStore)
                .embeddingModel(dashScopeEmbeddingModel)
                .maxResults(2)
                .minScore(0.7)
                // 👑 终极杀招：动态过滤器钩子！
                // 每次大模型需要检索时，都会回调这个 Lambda 表达式
                .dynamicFilter(query -> {
                    // 从当前线程的收纳盒里摸出前端传来的 category
                    String currentCategory = CategoryContextHolder.get();

                    if (currentCategory != null && !currentCategory.isEmpty()) {
                        System.out.println("🔍 [动态路由生效] 正在执行确定性漏斗拦截，约束分类: " + currentCategory);
                        // 动态拼装类似于 SQL 的 WHERE category = 'xxx'
                        return dev.langchain4j.store.embedding.filter.MetadataFilterBuilder
                                .metadataKey("category").isEqualTo(currentCategory);
                    }

                    // 如果没传分类，就不加过滤（或者您可以根据业务要求抛出异常）
                    return null;
                })
                .build();
    }

    /**
     *  RetrievalAugmentor - 用于内容检索的增强
     * @param contentRetriever
     * @param chatLanguageModel
     * @return
     */
    // 2. 👑 终极航母编队 (自动组装上述所有配件)
    @Bean
    public RetrievalAugmentor retrievalAugmentor(
            ContentRetriever contentRetriever,   // Spring 自动把上面的搬运工塞进来
            ChatLanguageModel chatLanguageModel) { // Spring 自动把大模型脑子塞进来

        // 🔪 第一步：制造“前额叶皮层” (查询压缩转换器)
        // 依赖陷阱解除：直接把 Spring 容器里的大模型脑子塞给它！
        // 🔪 局部组装：作为私有配件，不加 @Bean！保持容器干净！
        QueryTransformer queryTransformer = CompressingQueryTransformer.builder()
                .chatLanguageModel(chatLanguageModel)
                .build();

        // 🚀 第二步：组装“重型航母编队” (DefaultRetrievalAugmentor)
        // 将重写器和检索器严格按照先后顺序插入航母插槽！
        // 🚀 最终出厂：将重写器和检索器装入航母
        return DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer) // 上游：先重写
                .contentRetriever(contentRetriever) // 下游：再去捞数据
                .build();
    }

    // ==================== 🧠 双引擎架构：手动夺回大模型控制权 ====================

    // ==================== 🧠 终极双引擎架构 ====================

//    /**
//     * 1. 【基础干活脑】(qwen-plus) - 纯阻塞式
//     * 职责：负责工具调用、普通对话、轻量级路由 (Logistics, Refund, ReviewRisk, 阻塞的 Policy)
//     */
//    @Bean("defaultChatModel")
//    @Primary
//        public ChatLanguageModel defaultChatModel() {
//        return OpenAiChatModel.builder()
//                .baseUrl(dashScopeBaseUrl) // 从 yaml 注入
//                .apiKey(dashScopeApiKey)   // 从 yaml 注入
//                .modelName("qwen-plus")
//                .build();
//    }

    /**
     * 2. 【深度思考专家脑】(qwen3-32B) - 纯流式
     * 职责：专职负责规章制度的流式长篇解答 (PolicyStreamingAgent)
     */
    @Bean("thinkingStreamingChatModel")
    public StreamingChatLanguageModel thinkingStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(dashScopeBaseUrl)
                .apiKey(dashScopeApiKey)
                .modelName("qwq-32b-preview")
                .temperature(1.0)
                // 思考型模型通常不需要配太多花里胡哨的参数，保持默认即可
                .build();
    }
}
