package com.craft.frame.boot.web.model;

/**
 * @author DURR
 * @desc web请求信息收集类
 * @date 2023/6/25 20:22
 */
public class RequestInfo {

    private String uid;
    private String ip;

    public RequestInfo() {
    }

    public RequestInfo(String uid, String ip) {
        this.uid = uid;
        this.ip = ip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
