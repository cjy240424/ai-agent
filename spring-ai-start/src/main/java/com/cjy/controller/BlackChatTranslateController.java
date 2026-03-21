package com.cjy.controller;

import com.cjy.record.ZhuanBaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/blackChatTranslate")
@RequiredArgsConstructor
public class BlackChatTranslateController {

    private final ChatClient chatClient;

    @RequestMapping("/api/v1/ai/translate")
    public ZhuanBaoResponse translate(String message){
        //1. 实例化转换器
        BeanOutputConverter<ZhuanBaoResponse> converter = new BeanOutputConverter<>(ZhuanBaoResponse.class);

        //2. 获取勒令大模型输出JSON格式指令
        String format = converter.getFormat();

        //3.拼装模板
        PromptTemplate template = new PromptTemplate("""
        你是一个精通互联网大厂黑话的翻译专家。请将用户输入的原始大白话，翻译成包含“加班、牛马、优化、潜规则”等词汇的大厂黑话。
        用户输入的原始大白话是：{message}
        请严格按照以下数据结构输出，不要输出任何多余的解释：
        {format}
        """);

        //生成最终的信封——prompt
        Prompt prompt = template.create(Map.of(
                "message", message,
                "format", format
        ));

        String content = chatClient.prompt(prompt)
                .options(OpenAiChatOptions.builder()
                        .model("qwen-plus")
                        .build())
                .call()
                .content();

        // 4. 见证奇迹的时刻：把纯文本 JSON 瞬间转换成 Java 对象
        return converter.convert(content);
    }
}
