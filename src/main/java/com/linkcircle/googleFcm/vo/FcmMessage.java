package com.linkcircle.googleFcm.vo;

import com.google.common.collect.ImmutableList;
import com.google.firebase.internal.NonNull;
import lombok.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author suahe
 * @date 2022/10/20
 * @ApiNote
 */
@Data
public class FcmMessage {

    //appId(只是作为key为了查找app对应的FirebaseApp信息使用)
    private String appId;

    //标题
    private String title;

    //内容
    private String body;

    //客户端token，最多500个
    private List<String> tokens;

    //主题
    private String topic;

    //数据
    private Map<String, String> data;

    private FcmMessage(FcmMessage.Builder builder) {
        this.appId = builder.appId;
        this.title = builder.title;
        this.body = builder.body;
        this.tokens = builder.tokens.build();
        this.topic = builder.topic;
        this.data = builder.data;
    }

    public static FcmMessage.Builder builder() {
        return new FcmMessage.Builder();
    }

    public static class Builder {
        private ImmutableList.Builder<String> tokens;
        private Map<String, String> data;
        private String appId;
        private String title;
        private String body;
        private String topic;

        private Builder() {
            this.tokens = ImmutableList.builder();
            this.data = new HashMap<>();
        }

        public FcmMessage build() {
            return new FcmMessage(this);
        }

        public FcmMessage.Builder setAppId(@NonNull String appId) {
            this.appId = appId;
            return this;
        }

        public FcmMessage.Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public FcmMessage.Builder setBody(@NonNull String body) {
            this.body = body;
            return this;
        }

        public FcmMessage.Builder addToken(@NonNull String token) {
            this.tokens.add(token);
            return this;
        }

        public FcmMessage.Builder addAllTokens(@NonNull Collection<String> tokens) {
            this.tokens.addAll(tokens);
            return this;
        }

        public FcmMessage.Builder setTopic(@NonNull String topic) {
            this.topic = topic;
            return this;
        }

        public FcmMessage.Builder putData(@NonNull String key, @NonNull String value) {
            this.data.put(key, value);
            return this;
        }

        public FcmMessage.Builder putAllData(@NonNull Map<String, String> map) {
            this.data.putAll(map);
            return this;
        }
    }
}
