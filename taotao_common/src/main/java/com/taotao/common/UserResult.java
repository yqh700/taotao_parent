package com.taotao.common;

/**
 * Created by 杨清华.
 * on 2017/11/14.
 */
public class UserResult {

    private String status;

    public UserResult(String status) {
        this.status = status;
    }

    public UserResult() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
