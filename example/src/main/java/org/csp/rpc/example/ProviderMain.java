package org.csp.rpc.example;


import org.csp.rpc.registry.api.ProviderConfigBean;
import org.csp.rpc.registry.local.LocalBootstrap;
import org.csp.rpc.registry.local.LocalRegistryCenter;

public class ProviderMain {
    public static void main(String[] args) {
        LocalRegistryCenter registry = new LocalRegistryCenter();
        ProviderConfigBean bean = new ProviderConfigBean();
        bean.setName("provider-test");
        bean.setPort(12345);
        LocalBootstrap localBootstrap = new LocalBootstrap(registry, bean);
        localBootstrap.registry();
    }
}
