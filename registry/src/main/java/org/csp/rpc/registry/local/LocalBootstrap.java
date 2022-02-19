package org.csp.rpc.registry.local;

import org.csp.rpc.registry.api.NodeMetaInfo;
import org.csp.rpc.registry.api.ProviderConfigBean;
import org.csp.rpc.registry.api.RegistryCenter;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalBootstrap {

    private RegistryCenter registryCenter;
    private ProviderConfigBean providerConfigBean;

    public LocalBootstrap(RegistryCenter registryCenter, ProviderConfigBean providerConfigBean) {
        this.registryCenter = registryCenter;
        this.providerConfigBean = providerConfigBean;
    }

    public void registry() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostAddress = localHost.getHostAddress();
            NodeMetaInfo metaInfo = new NodeMetaInfo();
            metaInfo.setIp(hostAddress);
            metaInfo.setPort(providerConfigBean.getPort());
            registryCenter.registry(metaInfo);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
