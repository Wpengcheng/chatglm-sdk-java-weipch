package com.weipch.chatglm.executor.result;

import okhttp3.sse.EventSourceListener;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 11:00
 * @Description
 */
public interface ResultHandler {

    EventSourceListener eventSourceListener(EventSourceListener eventSourceListener);


}
