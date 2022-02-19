package org.csp.rpc.registry.api;

import java.io.Serializable;

public class Message implements Serializable {
    private Object msg;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
