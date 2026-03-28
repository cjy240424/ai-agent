package com.cjy.controller;

import com.cjy.service.PolicyStreamingAgent;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/policy")
@RequiredArgsConstructor
@CrossOrigin // 👑 加上这个，允许前端 HTML 直接调用！
public class PolicyStreamingController {

    private final PolicyStreamingAgent policyStreamingAgent;

    @GetMapping(value = "/{userId}/streamAsk", produces = "text/event-stream;charset=utf-8")
    public SseEmitter askStream(
            @PathVariable String userId,
            @RequestParam String question
    ){
        // 1. 造水管：强制设置超时时间为 5 分钟 (300000毫秒)，防止模型思考时被 Tomcat 误杀
        SseEmitter emitter = new SseEmitter(300000L);

        // 2. 启动流式引擎 (注意：这一步是异步的，主线程瞬间滑过)
        TokenStream tokenStream = policyStreamingAgent.anwerser(userId, question);

        // 3. 完美适配：将 AI 的心跳绑定到水管上
        tokenStream
                .onPartialResponse(token -> {
                    try {
                        // 模型吐出一个字，立刻顺着水管砸向前端！
                        // 👑 终极架构绝杀：不要光秃秃地 send！
                        // 强行指定 MediaType，逼迫底层的 HttpMessageConverter 必须使用 UTF-8 将汉字转码！
                        emitter.send(token, MediaType.valueOf("text/plain;charset=UTF-8"));
                    } catch (IOException e) {
                        // 物理容错：如果用户突然关掉浏览器页面，水管断裂，这里负责优雅捕获，防止后台疯狂报错
                        e.printStackTrace();
                    }
                })
                .onCompleteResponse(response -> {
                    // 模型说：我全部讲完了！
                    // 此时是回收元数据（如 Token 消耗量）的唯一安全时机！
                    System.out.println("\n✅ [流式传输完毕] 耗费 Token 总量: " + response.tokenUsage().totalTokenCount());

                    // 物理拔管：通知 Spring 和浏览器正常关闭连接
                    emitter.complete();
                })
                .onError(error -> {
                    // 发生极端异常（如百炼 API 欠费、网络中断）
                    System.err.println("❌ 模型调用毁灭性失败: " + error.getMessage());
                    emitter.completeWithError(error);
                })
                .start();
        return emitter;
    }

}
