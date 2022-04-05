package org.csp.rpc.bootstrap;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.csp.rpc.common.NetUtils;
import org.csp.rpc.registry.api.NodeMetaInfo;
import org.csp.rpc.registry.api.RegistryCenter;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * config prefix rpc.service
 *
 * @see Service
 * @see org.csp.rpc.bootstrap.ConfigManager.ConfigKeyEnum#SERVICE
 */
public class ServiceConfig extends AbstractConfig {

    private Class<?> reference;
    private Class<?> interfaceClass;
    private String interfaceName;
    private Integer timeout;
    private Integer retires;
    private Integer weight;

    private static final String [] excludeFields = {"interfaceClass", "reference", "interfaceName"};

    public Class<?> getReference() {
        return reference;
    }

    public void setReference(Class<?> reference) {
        this.reference = reference;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public int getRetires() {
        return retires;
    }

    public void setRetires(Integer retires) {
        this.retires = retires;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * export service.
     * registry yourself with the registry
     */
    public void export(RegistryCenter registryCenter, Integer port) {
        InetAddress localAddress = NetUtils.getLocalAddress();
        String address = localAddress.getHostAddress();

        Map<String, String> map = new HashMap<>();

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();

            if (hitExcludeFields(name)) {
                continue;
            }

            Object v = ReflectUtil.getFieldValue(this, field);
            
            map.put(name, String.valueOf(v));
        }

        NodeMetaInfo info = new NodeMetaInfo();
        info.setIp(address);
        info.setPort(port);
        info.setPayload(map);
        info.setServiceName(interfaceClass.getName());
        registryCenter.registry(info);
    }

    private boolean hitExcludeFields(String name) {
        for (String excludeField : excludeFields) {
            if (StrUtil.equals(excludeField, name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String configKey() {
        return ConfigManager.ConfigKeyEnum.SERVICE.getName();
    }

    @Override
    protected String configPrefix() {
        return  ConfigManager.ConfigKeyEnum.SERVICE.getConfigPrefix();
    }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "reference=" + reference +
                ", interfaceClass=" + interfaceClass +
                ", interfaceName='" + interfaceName + '\'' +
                ", timeout=" + timeout +
                ", retires=" + retires +
                ", weight=" + weight +
                '}';
    }

    public static class ServiceConfigBuilder {

        private ServiceConfig serviceConfig = new ServiceConfig();

        public ServiceConfigBuilder reference(Class<?> reference) {
            serviceConfig.reference = reference;
            return this;
        }

        public ServiceConfigBuilder interfaceClass(Class<?> interfaceClass) {
            serviceConfig.interfaceClass = interfaceClass;
            return this;
        }

        public ServiceConfigBuilder interfaceName(String interfaceName) {
            serviceConfig.interfaceName = interfaceName;
            return this;
        }

        public ServiceConfigBuilder timeout(Integer timeout) {
            serviceConfig.timeout = timeout;
            return this;
        }

        public ServiceConfigBuilder retires(Integer retires) {
            serviceConfig.retires = retires;
            return this;
        }

        public ServiceConfigBuilder weight(Integer weight) {
            serviceConfig.weight = weight;
            return this;
        }

        public ServiceConfig builder() {
            return serviceConfig;
        }
    }
}
