package com.zjw.spring.mvc.bean;

import java.lang.reflect.Method;

public class RequestMethod {
    /**
     * 请求类型：GET\POST\PUT\DELETE....
     */
    private String type;
    private Method method;

    public Method getMethod() {
        return method;
    }

    public String getType() {
        return type;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setType(String type) {
        this.type = type;
    }
}
