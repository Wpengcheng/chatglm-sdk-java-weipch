package com.weipch.chatglm.session;

import com.weipch.chatglm.medel.ChatCompletionRequest;
import com.weipch.chatglm.medel.ChatCompletionSyncResponse;
import com.weipch.chatglm.medel.ImageCompletionRequest;
import com.weipch.chatglm.medel.ImageCompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:22
 * @Description 会话接口
 */
public interface OpenAiSession {


    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception;

    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws Exception;

    ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception;

    ImageCompletionResponse genImages(ImageCompletionRequest imageCompletionRequest) throws Exception;

    Configuration configuration();

}
