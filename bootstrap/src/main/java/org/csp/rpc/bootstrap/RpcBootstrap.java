package org.csp.rpc.bootstrap;

import org.csp.rpc.core.extension.ExtensionLoader;
import org.csp.rpc.registry.api.RegistryCenter;
import org.csp.rpc.registry.zk.ZooKeeperRegistryCenter;

import java.net.InetAddress;
import java.util.Objects;

/**
 * Rpc application bootstrap
 */
public class RpcBootstrap {

    private ConfigManager configManager = ConfigManager.getInstance();

    private RegistryCenter registryCenter;

    private static class RpcBootstrapHolder {
        private static final RpcBootstrap INSTANCE = new RpcBootstrap();
    }

    public static RpcBootstrap getInstance() {
        return RpcBootstrapHolder.INSTANCE;
    }

    private RpcBootstrap() {
        init();
    }

    private void init() {
        initRegistryCenter();
    }

    private void initRegistryCenter() {
    }

    public void close() {
        
    }

    public void start() {
        connectRegistryCenter();

        exportServices();

        references();
    }

    private void connectRegistryCenter() {
        ExtensionLoader<RegistryCenter> extensionLoader = ExtensionLoader.loadExtensionInfo(RegistryCenter.class);

        RegistryCenter center = extensionLoader.getDefault();

        RegistryConfig registryConfig = configManager.getRegistryConfig();
        if (Objects.isNull(registryConfig)) {
            throw new RuntimeException("not found registry config");
        }

        center.connect(registryConfig.getHost());

        this.registryCenter = center;
    }

    private void references() {
        // TODO: 2022/1/16
    }

    private void exportServices() {
        MetaDataConfig metaDataConfig = configManager.getMetaData();
        configManager.getServices().forEach(service -> {
            ServiceConfig s = (ServiceConfig) service;
            s.export(this.registryCenter, metaDataConfig.getPort());
        });
    }
}
