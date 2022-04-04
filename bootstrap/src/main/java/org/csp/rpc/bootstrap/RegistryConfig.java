package org.csp.rpc.bootstrap;

/**
 * config prefix rpc.registry
 *
 * @see org.csp.rpc.bootstrap.ConfigManager.ConfigKeyEnum#REGISTRY
 */
public class RegistryConfig extends AbstractConfig {

    /**
     * Multiple are separated by ,
     * such as 127.0.0.1:2181,192.168.0.1:2181
     */
    private String host;

    @Override
    protected String configKey() {
        return ConfigManager.ConfigKeyEnum.REGISTRY.getName();
    }

    @Override
    protected String configPrefix() {
        return ConfigManager.ConfigKeyEnum.REGISTRY.getConfigPrefix();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "RegistryConfig{" +
                "host='" + host + '\'' +
                '}';
    }

    public static class RegistryConfigBuilder {
        private RegistryConfig registryConfig = new RegistryConfig();

        public RegistryConfigBuilder host(String host) {
            registryConfig.host = host;
            return this;
        }

        public RegistryConfig builder() {
            return registryConfig;
        }
    }
}
