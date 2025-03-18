package com.weipch.chatglm.session;

import com.weipch.chatglm.IOpenAiApi;
import com.weipch.chatglm.executor.Executor;
import com.weipch.chatglm.executor.aigc.GLMExecutor;
import com.weipch.chatglm.executor.aigc.GLMOldExecutor;
import com.weipch.chatglm.medel.Model;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * @Author 方唐镜
 * @Date 2025-03-17 22:37
 * @Description 配置文件
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {


    @Getter
    @Setter
    private String apiHost = "https://open.bigmodel.cn/";

    @Getter
    @NotNull
    private String apiKey;

    // 智谱：apiSecretKey = {apiKey}.{apiSecret}
    // apiSecretKey：74cc430e842445668d37bc0e3ef1ac6b.9AjvM32aR1n98ahv
    private String apiSecretKey;

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
        String[] arrStr = apiSecretKey.split("\\.");
        if (arrStr.length != 2) {
            throw new RuntimeException("invalid apiSecretKey");
        }
        this.apiKey = arrStr[0];
        this.apiSecret = arrStr[1];
    }

    @Getter
    private String apiSecret;

    @Getter
    @Setter
    private IOpenAiApi openAiApi;

    @Getter
    @Setter
    private OkHttpClient okHttpClient;


    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }

    private HashMap<Model, Executor> executorGroup;

    // http keywords
    public static final String SSE_CONTENT_TYPE = "text/event-stream";
    public static final String DEFAULT_USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    public static final String APPLICATION_JSON = "application/json";
    public static final String JSON_CONTENT_TYPE = APPLICATION_JSON + "; charset=utf-8";

    public HashMap<Model, Executor> newExecutorGroup() {
        this.executorGroup = new HashMap<>();
        // 旧版模型，兼容
        Executor glmOldExecutor = new GLMOldExecutor(this);
        this.executorGroup.put(Model.CHATGLM_6B_SSE, glmOldExecutor);
        this.executorGroup.put(Model.CHATGLM_LITE, glmOldExecutor);
        this.executorGroup.put(Model.CHATGLM_LITE_32K, glmOldExecutor);
        this.executorGroup.put(Model.CHATGLM_STD, glmOldExecutor);
        this.executorGroup.put(Model.CHATGLM_PRO, glmOldExecutor);
        this.executorGroup.put(Model.CHATGLM_TURBO, glmOldExecutor);
        // 新版模型，配置
        Executor glmExecutor = new GLMExecutor(this);
        this.executorGroup.put(Model.GLM_3_5_TURBO, glmExecutor);
        this.executorGroup.put(Model.GLM_4, glmExecutor);
        this.executorGroup.put(Model.GLM_4V, glmExecutor);
        this.executorGroup.put(Model.GLM_4_Plus, glmExecutor);
        this.executorGroup.put(Model.GLM_4_0520, glmExecutor);
        this.executorGroup.put(Model.GLM_4_Lng, glmExecutor);
        this.executorGroup.put(Model.GLM_4_AirX, glmExecutor);
        this.executorGroup.put(Model.GLM_4_Air, glmExecutor);
        this.executorGroup.put(Model.GLM_4_FlashX, glmExecutor);
        this.executorGroup.put(Model.GLM_4_Flash, glmExecutor);
        this.executorGroup.put(Model.GLM_4_AllTools, glmExecutor);
        this.executorGroup.put(Model.COGVIEW_3, glmExecutor);
        return this.executorGroup;
    }


}
