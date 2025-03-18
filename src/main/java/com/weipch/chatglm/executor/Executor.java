package com.weipch.chatglm.executor;

import com.weipch.chatglm.medel.ChatCompletionRequest;
import com.weipch.chatglm.medel.ChatCompletionSyncResponse;
import com.weipch.chatglm.medel.ImageCompletionRequest;
import com.weipch.chatglm.medel.ImageCompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:35
 * @Description
 */
public interface Executor {

    /**
     * 问答模式，流式反馈
     *
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener   实现监听；通过监听的 onEvent 方法接收数据
     * @return 应答结果
     * @throws Exception 异常
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception;

    /**
     * 问答模式，同步反馈 —— 用流式转化 Future
     *
     * @param chatCompletionRequest 请求信息
     * @return 应答结果
     */
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

    /**
     * 同步应答接口
     *
     * @param chatCompletionRequest 请求信息
     * @return ChatCompletionSyncResponse
     * @throws IOException 异常
     */
    ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception;

    /**
     * 图片生成接口
     *
     * @param request 请求信息
     * @return 应答结果
     */
    ImageCompletionResponse genImages(ImageCompletionRequest request) throws Exception;

}
