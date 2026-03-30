package com.cjy.service;

import dev.langchain4j.service.TokenStream;

public interface PolicyStreamingService {
    TokenStream getDynamicPolicyStream(String userId, String question, String category);
}
