package com.weipch.chatglm.interceptor;

import com.weipch.chatglm.session.Configuration;
import com.weipch.chatglm.utils.BearerTokenUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:25
 * @Description
 */
public class OpenAiInterceptor implements Interceptor {



    private final Configuration configuration;

    public OpenAiInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }



    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                .header("Content-Type", Configuration.JSON_CONTENT_TYPE)
                .header("User-Agent", Configuration.DEFAULT_USER_AGENT)
//                .header("Accept", null != original.header("Accept") ? original.header("Accept") : Configuration.SSE_CONTENT_TYPE)
                .method(originalRequest.method(), originalRequest.body())
                .build();

        return chain.proceed(request);
    }
}
