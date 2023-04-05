package com.linkcircle.googleFcm.controller;

import com.google.firebase.messaging.BatchResponse;
import com.linkcircle.googleFcm.common.CommonConstant;
import com.linkcircle.googleFcm.common.FcmEventType;
import com.linkcircle.googleFcm.util.FcmUtil;
import com.linkcircle.googleFcm.vo.FcmMessage;
import com.linkcircle.googleFcm.vo.FcmMessageReq;
import com.linkcircle.googleFcm.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suahe
 * @date 2022/10/20
 * @ApiNote
 */
@RequestMapping("/fcm")
@RestController
public class FcmController {

    @PostMapping("/pushAndroidByTokens")
    public Result<?> pushAndroidByTokens(@RequestBody FcmMessageReq fcmMessageReq) {
        FcmMessage fcmMessage = FcmMessage.builder().setAppId(CommonConstant.APP_ID).setTitle(fcmMessageReq.getTitle())
                .setBody(fcmMessageReq.getBody()).addToken(fcmMessageReq.getToken())
                .putData(CommonConstant.EVENT_TYPE, FcmEventType.NOTICE.getCode())
                .putData(CommonConstant.EVENT_NAME, FcmEventType.NOTICE.getDesc())
                .putData(CommonConstant.BUSINESS_NAME, "id111")
                .build();
        BatchResponse batchResponse = FcmUtil.pushAndroidByTokens(fcmMessage);
        return Result.OK(batchResponse);
    }

    @PostMapping("/pushAndroidByTopic")
    public Result<?> pushAndroidByTopic(@RequestBody FcmMessageReq fcmMessageReq) {
        FcmMessage fcmMessage = FcmMessage.builder().setAppId(CommonConstant.APP_ID).setTitle(fcmMessageReq.getTitle())
                .setBody(fcmMessageReq.getBody()).setTopic(fcmMessageReq.getTopic())
                .putData(CommonConstant.EVENT_TYPE, FcmEventType.NOTICE.getCode())
                .putData(CommonConstant.EVENT_NAME, FcmEventType.NOTICE.getDesc())
                .putData(CommonConstant.BUSINESS_NAME, "id111")
                .build();
        String response = FcmUtil.pushAndroidByTopic(fcmMessage);
        return Result.OK(response);
    }

    @PostMapping("/pushIosByTokens")
    public Result<?> pushIosByTokens(@RequestBody FcmMessageReq fcmMessageReq) {
        FcmMessage fcmMessage = FcmMessage.builder().setAppId(CommonConstant.APP_ID).setTitle(fcmMessageReq.getTitle())
                .setBody(fcmMessageReq.getBody()).addToken(fcmMessageReq.getToken())
                .putData(CommonConstant.EVENT_TYPE, FcmEventType.NOTICE.getCode())
                .putData(CommonConstant.EVENT_NAME, FcmEventType.NOTICE.getDesc())
                .putData(CommonConstant.BUSINESS_NAME, "id111")
                .build();
        BatchResponse batchResponse = FcmUtil.pushIosByTokens(fcmMessage);
        return Result.OK(batchResponse);
    }

    @PostMapping("/pushIosByTopic")
    public Result<?> pushIosByTopic(@RequestBody FcmMessageReq fcmMessageReq) {
        FcmMessage fcmMessage = FcmMessage.builder().setAppId(CommonConstant.APP_ID).setTitle(fcmMessageReq.getTitle())
                .setBody(fcmMessageReq.getBody()).setTopic(fcmMessageReq.getTopic())
                .putData(CommonConstant.EVENT_TYPE, FcmEventType.NOTICE.getCode())
                .putData(CommonConstant.EVENT_NAME, FcmEventType.NOTICE.getDesc())
                .putData(CommonConstant.BUSINESS_NAME, "id111")
                .build();
        String response = FcmUtil.pushIosByTopic(fcmMessage);
        return Result.OK(response);
    }
}
