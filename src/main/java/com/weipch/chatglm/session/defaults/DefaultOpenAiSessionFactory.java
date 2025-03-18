package com.weipch.chatglm.session.defaults;

import com.weipch.chatglm.IOpenAiApi;
import com.weipch.chatglm.executor.Executor;
import com.weipch.chatglm.interceptor.OpenAiInterceptor;
import com.weipch.chatglm.medel.Model;
import com.weipch.chatglm.session.Configuration;
import com.weipch.chatglm.session.OpenAiSession;
import com.weipch.chatglm.session.OpenAiSessionFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author 方唐镜
 * @Date 2025-03-18 10:20
 * @Description OpenAi会话工厂
 */
public class DefaultOpenAiSessionFactory implements OpenAiSessionFactory {

    private final Configuration configuration;

    public DefaultOpenAiSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public OpenAiSession openAiSession() {
        // 1.日志配置
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 2.开启并配置Http客户端
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiInterceptor(configuration))
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();

        configuration.setOkHttpClient(okHttpClient);

        // 3.创建Api服务
        IOpenAiApi openAiApi = new Retrofit.Builder()
                .baseUrl(configuration.getApiHost())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(IOpenAiApi.class);
        configuration.setOpenAiApi(openAiApi);

        // 4. 实例化执行器
        HashMap<Model, Executor> executorGroup = configuration.newExecutorGroup();

        return new DefaultOpenAiSession(configuration, executorGroup);

    }


}
