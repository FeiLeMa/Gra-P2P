package com.alag.p2p.business.core.common.response;

/**
 * @author alanagou@aliyun.com
 * @Title: ResponseCode
 * @ProjectName com.alanagou.p2p.business
 * @Description:
 * @date 2019/2/1  11:36
 */
public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(2,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(3,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
