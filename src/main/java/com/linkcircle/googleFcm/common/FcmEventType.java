package com.linkcircle.googleFcm.common;

/**
 * @author suahe
 * @date 2022/10/20
 * @ApiNote fcm事件枚举
 */
public enum FcmEventType {
    NOTICE("0", "公告"),
    ;

    private String code;

    private String desc;

    FcmEventType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
