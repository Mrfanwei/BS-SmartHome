package com.smartlife.qintin.model;

/**
 * 描述：
 * 作者：傅健
 * 创建时间：2017/7/31 15:34
 */

public class ErrorModel {

    /**
     * token不存在
     */
    public static final int TOKEN_NOT_FOUND = 20001;

    /**
     * token过期
     */
    public static final int TOKEN_EXPIRED = 20002;

    /**
     * 超过访问频率
     */
    public static final int RATE_EXCEED_LIMIT = 20003;

    /**
     * 超出许可权限
     */
    public static final int SCOPE_NOT_PERMITTED = 20004;

    /**
     * 找不到该页面
     */
    public static final int URL_NOT_FOUND = 20005;

    /**
     * 无效的请求
     */
    public static final int INVALID_REQUEST = 20006;

    /**
     * errorno : 20001
     * errormsg : token_not_found
     */

    private int errorno;
    private String errormsg;

    public int getErrorno() {
        return errorno;
    }

    public void setErrorno(int errorno) {
        this.errorno = errorno;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "errorno=" + errorno +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }
}
