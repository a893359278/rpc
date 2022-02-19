package org.csp.rpc.bootstrap;

import javax.annotation.PostConstruct;

public abstract class AbstractConfig {


    /**
     * During initialization, add configuration to start the container.
     */
    @PostConstruct
    public void addConfig() {
        ConfigManager.getInstance().addConfig(configKey(), this);
    }

    /**
     * key in configManager
     * @return
     */
    protected abstract String configKey();

    /**
     * config prefix.
     * @return
     */
    protected abstract String configPrefix();
}
