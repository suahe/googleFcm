package com.linkcircle.googleFcm.common;

public class CommonConstant {

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;
    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;
    /**
     * googleFcm appId
     **/
    public static final String APP_ID = "google_fcm";

    /**
     * googleFcm topic
     */
    public static final String TOPIC = "google_fcm";
    /**
     * 事件类型
     */
    public static final String EVENT_TYPE = "eventType";
    /**
     * 事件名称
     */
    public static final String EVENT_NAME = "eventName";
    /**
     * 业务ID
     */
    public static final String BUSINESS_NAME = "businessId";
}
