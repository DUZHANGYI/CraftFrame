package com.craft.frame.boot.web.security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author DURR
 * @desc 类描述
 * @date 2023/6/24 14:35
 */

@ConfigurationProperties(prefix = "security.config")
public class SecurityProperties {

    private boolean enable = false;

    /**
     * Security 不拦截接口
     */
    private String[] ignoreInterface = new String[0];

    /**
     * 退出接口
     */
    private String logoutPath;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public SecurityProperties() {
    }

    public String[] getIgnoreInterface() {
        return ignoreInterface;
    }

    public void setIgnoreInterface(String[] ignoreInterface) {
        this.ignoreInterface = ignoreInterface;
    }

    public String getLogoutPath() {
        return logoutPath;
    }

    public void setLogoutPath(String logoutPath) {
        this.logoutPath = logoutPath;
    }
}
