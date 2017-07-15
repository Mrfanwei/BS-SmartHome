package com.smartlife.qintin.model;

/**
 * Created by user on 2017/5/5.
 */

public class CredentialModel {

    /**
     * access_token : ZmZjODE4NjItNWE5NS00M2JmLWIxY
     * token_type : bearer
     * expires_in : 7200
     */

    private String access_token;
    private String token_type;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
