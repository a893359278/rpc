package org.csp.rpc.core.registry;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private Map<String, Object> map = new HashMap<>();

    private ServiceRegistry() {}

    private static final ServiceRegistry INSTANCE = new ServiceRegistry();

    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }

    public void registry(Object obj) {
        map.put(obj.getClass().getInterfaces()[0].getName(), obj);
    }

    public Object getService(String fullClsName) {
        return map.get(fullClsName);
    }
}
