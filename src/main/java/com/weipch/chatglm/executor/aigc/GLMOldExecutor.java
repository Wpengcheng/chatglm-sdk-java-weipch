package com.weipch.chatglm.executor.aigc;

import com.alibaba.fastjson.JSON;
import com.weipch.chatglm.IOpenAiApi;
import com.weipch.chatglm.executor.Executor;
import com.weipch.chatglm.medel.*;
import com.weipch.chatglm.session.Configuration;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:34
 * @Description GLM旧版执行器
 */
public class GLMOldExecutor implements Executor {

    private final Configuration configuration;

    private final EventSource.Factory factory;

    public GLMOldExecutor(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();

        // 返回事件结果
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();

        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        // 异步响应请求
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.add.getCode().equals(type)) {
                    dataBuffer.append(response.getData());
                } else if (EventType.finish.getCode().equals(type)) {
                    future.complete(dataBuffer.toString());
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });
        return future;
    }

    @Override
    public ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions_sync).replace("{model}", chatCompletionRequest.getModel().getCode()))
                .header("Accept",Configuration.APPLICATION_JSON)
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        OkHttpClient okHttpClient = configuration.getOkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            new RuntimeException("Request failed");
        }
        return JSON.parseObject(response.body().string(),ChatCompletionSyncResponse.class);
    }

    @Override
    public ImageCompletionResponse genImages(ImageCompletionRequest request) throws Exception {
        throw new RuntimeException("旧版无图片生成接口");
    }
}
