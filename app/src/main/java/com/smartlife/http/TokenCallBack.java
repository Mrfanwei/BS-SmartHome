package com.smartlife.http;

/**
 * 描述：
 * 作者：傅健
 * 创建时间：2017/7/31 10:53
 */

public interface TokenCallBack {

    void onResponse();

    void onError(String s);

    void onEmpty();
}
