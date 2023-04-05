package com.linkcircle.googleFcm.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author suahe
 * @date 2022/10/20
 * @ApiNote
 */
@Data
public class FcmMessageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String body;

    private String token;

    private String topic;
}
