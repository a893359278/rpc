package org.csp.rpc.remoting.handler;

import java.io.Serializable;
import java.util.Arrays;

public class RpcInvokerParam implements Serializable {

    private static final long serialVersionUID = 3515962228886841564L;

    private String cls;

    private String method;

    private Class<?> [] parameters;

    private Object[] args;

    public RpcInvokerParam(String cls, String method, Class<?>[] parameters, Object[] args) {
        this.cls = cls;
        this.method = method;
        this.parameters = parameters;
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }


    @Override
    public String toString() {
        return "RpcInvokerParam{" +
                "cls='" + cls + '\'' +
                ", method='" + method + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
