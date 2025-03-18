package com.weipch.chatglm.session.defaults;

import com.weipch.chatglm.executor.Executor;
import com.weipch.chatglm.medel.*;
import com.weipch.chatglm.session.Configuration;
import com.weipch.chatglm.session.OpenAiSession;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:30
 * @Description
 */
public class DefaultOpenAiSession implements OpenAiSession {


    private final Configuration configuration;

    private final Map<Model, Executor> executorGroup;


    public DefaultOpenAiSession(Configuration configuration, Map<Model, Executor> executorGroup) {
        this.configuration = configuration;
        this.executorGroup = executorGroup;
    }


    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws Exception {
        Executor executor = executorGroup.get(chatCompletionRequest.getModel());
        if (null == executor) throw new RuntimeException(chatCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.completions(chatCompletionRequest, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws Exception {
        Executor executor = executorGroup.get(chatCompletionRequest.getModel());
        if (null == executor) throw new RuntimeException(chatCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.completions(chatCompletionRequest);    }

    @Override
    public ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws Exception {
        Executor executor = executorGroup.get(chatCompletionRequest.getModel());
        if (null == executor) throw new RuntimeException(chatCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.completionsSync(chatCompletionRequest);    }

    @Override
    public ImageCompletionResponse genImages(ImageCompletionRequest imageCompletionRequest) throws Exception {
        Executor executor = executorGroup.get(imageCompletionRequest.getModelEnum());
        if (null == executor) throw new RuntimeException(imageCompletionRequest.getModel() + " 模型执行器尚未实现！");
        return executor.genImages(imageCompletionRequest);    }

    @Override
    public Configuration configuration() {
        return configuration;
    }
}
