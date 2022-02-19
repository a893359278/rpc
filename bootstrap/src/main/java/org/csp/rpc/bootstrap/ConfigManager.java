package org.csp.rpc.bootstrap;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.csp.rpc.bootstrap.ConfigManager.ConfigKeyEnum.*;

public class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();

    private final ConcurrentMap<String, List<AbstractConfig>> configs = new ConcurrentHashMap<>(16);

    private ConfigManager() {}

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public void addConfig(String key, AbstractConfig config) {
        configs.compute(key, (k, v) -> {
            if (CollectionUtil.isEmpty(v)) {
                List<AbstractConfig> list = new ArrayList<>();
                list.add(config);
                return list;
            } else {
                v.add(config);
                return v;
            }
        });
    }

    public List<AbstractConfig> getServices() {
        return configs.getOrDefault(SERVICE.getName(), new ArrayList<>());
    }

    public RegistryConfig getRegistryConfig() {
        return doGetSingleConfig(REGISTRY.getName());
    }

    public MetaDataConfig getMetaData() {
        return doGetSingleConfig(META_DATA.getName());
    }

    private <T extends AbstractConfig> T doGetSingleConfig(String key) {
        List<AbstractConfig> list = configs.getOrDefault(key, new ArrayList<>());
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return (T) list.get(0);
    }


    public enum ConfigKeyEnum {
        SERVICE("service", "rpc.service"),
        REGISTRY("registry", "rpc.registry"),
        META_DATA("metaData", "rpc.metaData"),
        ;

        ConfigKeyEnum(String name, String configPrefix) {
            this.name = name;
            this.configPrefix = configPrefix;
        }

        private final String name;
        private final String configPrefix;

        public String getName() {
            return name;
        }

        public String getConfigPrefix() {
            return configPrefix;
        }
    }
}
