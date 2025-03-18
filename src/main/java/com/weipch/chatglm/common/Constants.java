package com.weipch.chatglm.common;

/**
 * @Author 方唐镜
 * @Date 2025-03-17 22:09
 * @Description
 */
public class Constants {

    /**
     * 官网支持的请求角色类型；system、user、assistant
     * https://platform.openai.com/docs/guides/chat/introduction
     */
    public enum Role {

        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        ;

        private String code;

        Role(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }


}
