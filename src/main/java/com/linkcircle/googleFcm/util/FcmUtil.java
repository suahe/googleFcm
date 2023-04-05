package com.linkcircle.googleFcm.util;


import com.alibaba.fastjson.JSONObject;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.linkcircle.googleFcm.common.CommonConstant;
import com.linkcircle.googleFcm.common.FcmEventType;
import com.linkcircle.googleFcm.vo.BasicAuthenticator;
import com.linkcircle.googleFcm.vo.FcmMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FcmUtil {

    private static Map<String, FirebaseApp> firebaseAppMap = new ConcurrentHashMap<>();

    /**
     * @param host     ip地址
     * @param port     端口号
     * @param username 如果有则填
     * @param password 如果有则填
     */
    private static void initProxy(String host, int port, final String username, final String password) {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        //HTTP代理
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", Integer.toString(port));
        //HTTPS代理
        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", Integer.toString(port));

        if (username != null && password != null)
            Authenticator.setDefault(new BasicAuthenticator(username, password));
    }

    /**
     * 根据tokens推送安卓
     *
     * @param fcmMessage
     * @return
     */
    public static BatchResponse pushAndroidByTokens(FcmMessage fcmMessage) {
        log.info("pushAndroidByTokens fcmMessage：{}", JSONObject.toJSONString(fcmMessage));
        if (!isInit(fcmMessage.getAppId())) {
            initSDK(fcmMessage.getAppId());
        }
        FirebaseApp firebaseApp = firebaseAppMap.get(fcmMessage.getAppId());
        BatchResponse batchResponse = null;
        try {
            if (firebaseApp != null) {
                AndroidConfig androidConfig = getAndroidConfig(fcmMessage);

                //在进行消息发送之前要设置代理  这个非常重要，因为访问谷歌的服务器需要通过代理服务器在进行访问
                //initProxy("122.11.168.38",22,"conversant","q*6Mdh#VNMLuV56M");

                //构建消息
                MulticastMessage message = MulticastMessage.builder()
                        .setNotification(Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody()).build())
                        .addAllTokens(fcmMessage.getTokens()) //向所有的设备推送消息
                        .setAndroidConfig(androidConfig)
                        .build();
                batchResponse = FirebaseMessaging.getInstance(firebaseApp).sendMulticast(message);
                log.info("pushAndroidByTokens return result：{}" + JSONObject.toJSONString(batchResponse));
            }
        } catch (Exception e) {
            log.error("pushAndroidByTokens error：{}", e);
        }
        return batchResponse;
    }

    /**
     * 根据主题推送安卓
     *
     * @param fcmMessage
     * @return
     */
    public static String pushAndroidByTopic(FcmMessage fcmMessage) {
        log.info("pushAndroidByTopic fcmMessage：{}", JSONObject.toJSONString(fcmMessage));
        if (!isInit(fcmMessage.getAppId())) {
            initSDK(fcmMessage.getAppId());
        }
        FirebaseApp firebaseApp = firebaseAppMap.get(fcmMessage.getAppId());
        String response = null;
        try {
            if (firebaseApp != null) {
                AndroidConfig androidConfig = getAndroidConfig(fcmMessage);
                //在进行消息发送之前要设置代理  这个非常重要，因为访问谷歌的服务器需要通过代理服务器在进行访问
                //initProxy("122.11.168.38",22,"conversant","q*6Mdh#VNMLuV56M");

                //构建消息
                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody()).build())
                        .setTopic(fcmMessage.getTopic())
                        .setAndroidConfig(androidConfig)
                        .build();
                response = FirebaseMessaging.getInstance(firebaseApp).send(message);
                log.info("pushAndroidByTopic return result：{}" + response);
            }
        } catch (Exception e) {
            log.error("pushAndroidByTopic error：{}", e);
        }
        return response;
    }

    private static AndroidConfig getAndroidConfig(FcmMessage fcmMessage) {
        //获取AndroidConfig.Builder对象
        AndroidConfig.Builder androidConfigBuilder = AndroidConfig.builder();
        //获取AndroidNotification.Builder对象
        AndroidNotification.Builder androidNotifBuilder = AndroidNotification.builder();
        //可以存放一个数据信息进行发送，使得app开发客户端可以接受信息
        if (fcmMessage.getData() != null && fcmMessage.getData().size() > 0) {
            fcmMessage.getData().entrySet().stream().forEach(m -> {
                androidConfigBuilder.putData(m.getKey(), m.getValue());
            });
        }
        //设置包名,ios/android在fcm官网注册获取到的包名
        //androidConfigBuilder.setRestrictedPackageName(packageName);
        //设置过期时间 官方文档以毫秒为单位
        androidConfigBuilder.setTtl(3600 * 1000);
        // 设置消息标题
        androidNotifBuilder.setTitle(fcmMessage.getTitle());
        // 设置消息内容
        androidNotifBuilder.setBody(fcmMessage.getBody());
        //点击消息后的触发事件 ，如果想要触发app可以默认设置为OPEN_STOCK_ACTIVITY
        //androidNotifiBuilder.setClickAction(clickAction);
        //设置发送的频道的id
        //androidNotifiBuilder.setChannelId(channelId);
        AndroidNotification androidNotification = androidNotifBuilder.build();
        androidConfigBuilder.setNotification(androidNotification);

        return androidConfigBuilder.build();
    }

    /**
     * 根据tokens推送ios
     *
     * @param fcmMessage
     * @return
     */
    public static BatchResponse pushIosByTokens(FcmMessage fcmMessage) {
        log.info("pushIosByTokens fcmMessage：{}", JSONObject.toJSONString(fcmMessage));
        if (!isInit(fcmMessage.getAppId())) {
            initSDK(fcmMessage.getAppId());
        }
        FirebaseApp firebaseApp = firebaseAppMap.get(fcmMessage.getAppId());
        BatchResponse batchResponse = null;
        try {
            if (firebaseApp != null) {
                ApnsConfig apnsConfig = getApsConfig(fcmMessage);

                //在进行消息发送之前要设置代理  这个非常重要，因为访问谷歌的服务器需要通过代理服务器在进行访问
                //initProxy("yourHost", 80, "yourUsername", "yourPassword");

                //构建消息
                MulticastMessage message = MulticastMessage.builder()
                        .setNotification(Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody()).build())
                        .addAllTokens(fcmMessage.getTokens()) //向所有的设备推送消息
                        .setApnsConfig(apnsConfig)
                        .build();
                batchResponse = FirebaseMessaging.getInstance(firebaseApp).sendMulticast(message);
                log.info("pushIosByTokens return result：{}" + JSONObject.toJSONString(batchResponse));
            }
        } catch (Exception e) {
            log.error("pushIosByTokens error：{}", e);
        }
        return batchResponse;
    }

    /**
     * 根据主题推送ios
     *
     * @param fcmMessage
     * @return
     */
    public static String pushIosByTopic(FcmMessage fcmMessage) {
        log.info("pushIosByTopic fcmMessage：{}", JSONObject.toJSONString(fcmMessage));
        if (!isInit(fcmMessage.getAppId())) {
            initSDK(fcmMessage.getAppId());
        }
        FirebaseApp firebaseApp = firebaseAppMap.get(fcmMessage.getAppId());
        String response = null;
        try {
            if (firebaseApp != null) {
                ApnsConfig apnsConfig = getApsConfig(fcmMessage);

                //在进行消息发送之前要设置代理  这个非常重要，因为访问谷歌的服务器需要通过代理服务器在进行访问
                //initProxy("yourHost", 80, "yourUsername", "yourPassword");

                //构建消息
                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody()).build())
                        .setTopic(fcmMessage.getTopic())
                        .setApnsConfig(apnsConfig)
                        .build();
                response = FirebaseMessaging.getInstance(firebaseApp).send(message);
                log.info("pushIosByTopic return result：{}" + response);
            }
        } catch (Exception e) {
            log.error("pushIosByTopic error：{}", e);
        }
        return response;
    }

    private static ApnsConfig getApsConfig(FcmMessage fcmMessage) {
        //获取ApnsConfig.Builder对象
        ApnsConfig.Builder apsConfigBuilder = ApnsConfig.builder();
        //可以存放一个数据信息进行发送，使得app开发客户端可以接受信息
        apsConfigBuilder.putHeader("apns-priority", "10");
        if (fcmMessage.getData() != null && fcmMessage.getData().size() > 0) {
            fcmMessage.getData().entrySet().stream().forEach(m -> {
                apsConfigBuilder.putCustomData(m.getKey(), m.getValue());
            });
        }
        ApsAlert.Builder apsAlertBuilder = ApsAlert.builder();
        // 设置消息标题
        apsAlertBuilder.setTitle(fcmMessage.getTitle());
        // 设置消息内容
        apsAlertBuilder.setBody(fcmMessage.getBody());
        Aps.Builder apsBuilder = Aps.builder();
        apsBuilder.setAlert(apsAlertBuilder.build());
        apsBuilder.setBadge(42);
        apsBuilder.setSound("beep.wav");
        apsConfigBuilder.setAps(apsBuilder.build());
        return apsConfigBuilder.build();
    }

    private static void initSDK(String appId) {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(getJsonInputStream()))
                    .build();
            //初始化firebaseApp
            FirebaseApp firebaseApp = null;
            try {
                firebaseApp = FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                firebaseApp = FirebaseApp.getInstance(appId);
                if (firebaseApp != null) {
                    firebaseApp.delete();
                    firebaseApp = FirebaseApp.initializeApp(options);
                }
            }
            //存放
            firebaseAppMap.put(appId, firebaseApp);
        } catch (IOException e) {
            log.error("initSDK appId：{}, error：{}", appId, e);
        }
    }

    private static boolean isInit(String appId) {
        return firebaseAppMap.containsKey(appId);
    }

    private static InputStream getJsonInputStream() {
        ClassPathResource classPathResource = new ClassPathResource("firebase-admin-sdk.json");
        try {
            return classPathResource.getInputStream();
        } catch (IOException e) {
            log.error("getJsonInputStream error：{}", e);
            throw new RuntimeException("not configured firebase-admin-sdk.json file");
        }
    }

    private static String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(getJsonInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/userinfo.profile",
                        "https://www.googleapis.com/auth/content"));
        googleCredentials.refreshAccessToken();
        String token = googleCredentials.getAccessToken().getTokenValue();
        System.out.println("token：" + token);
        return token;
    }

    public static void main(String[] args) {
        FcmMessage fcmMessage = FcmMessage.builder().setAppId(CommonConstant.APP_ID).setTitle("测试标题")
                .setBody("测试内容").setTopic(CommonConstant.TOPIC)
                .putData(CommonConstant.EVENT_TYPE, FcmEventType.NOTICE.getCode())
                .putData(CommonConstant.EVENT_NAME, FcmEventType.NOTICE.getDesc())
                .putData(CommonConstant.BUSINESS_NAME, null)
                .build();
        pushIosByTokens(fcmMessage);
    }
}
