package org.csp.rpc.bootstrap;

import java.util.Objects;

/**
 * config prefix rpc.metaData
 *
 * dubbo application MetaData
 *
 * @see org.csp.rpc.bootstrap.ConfigManager.ConfigKeyEnum#META_DATA
 */
public class MetaDataConfig extends AbstractConfig {

    private Integer port;

    @Override
    protected String configKey() {
        return ConfigManager.ConfigKeyEnum.META_DATA.getName();
    }

    @Override
    protected String configPrefix() {
        return ConfigManager.ConfigKeyEnum.META_DATA.getConfigPrefix();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        if (Objects.isNull(port)) {
            this.port = 10086;
        } else {
            this.port = port;
        }
    }

    public static class MetaDataConfigBuilder {
        private MetaDataConfig metaDataConfig = new MetaDataConfig();

        public MetaDataConfigBuilder port(Integer port) {
            metaDataConfig.port = port;
            return this;
        }

        public MetaDataConfig builder() {
            return metaDataConfig;
        }
    }
}
