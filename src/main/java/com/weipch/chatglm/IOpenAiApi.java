package com.weipch.chatglm;

import com.weipch.chatglm.medel.*;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:18
 * @Description OpenAi 接口，用于扩展通用类服务
 */
public interface IOpenAiApi {


    String v3_completions = "api/paas/v3/model-api/{model}/sse-invoke";
    String v3_completions_sync = "api/paas/v3/model-api/{model}/invoke";

    @POST(v3_completions)
    Single<ChatCompletionResponse> completions(@Path("model") String model, @Body ChatCompletionRequest chatCompletionRequest);

    @POST(v3_completions_sync)
    Single<ChatCompletionSyncResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);

    String v4 = "api/paas/v4/chat/completions";

    String cogview3 = "api/paas/v4/images/generations";

    @POST(cogview3)
    Single<ImageCompletionResponse> genImages(@Body ImageCompletionRequest imageCompletionRequest);

}
