package org.csp.rpc.registry.zk;

import cn.hutool.core.util.StrUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.csp.rpc.registry.api.NodeMetaInfo;
import org.csp.rpc.registry.api.RegistryCenter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.apache.curator.x.discovery.ServiceInstance.builder;

public class ZooKeeperRegistryCenter implements RegistryCenter {

    CuratorFramework client;

    private String PATH = "/rpc/services";

    private ServiceDiscovery<ZookeeperInstance> serviceDiscovery;

    /**
     * default construct so that it can be instantiated(reflect).
     */
    public ZooKeeperRegistryCenter() {
    }

    @Override
    public void connect(String discoveryAddress) {
        try {
            if (StrUtil.isBlank(discoveryAddress)) {
                discoveryAddress = "localhost:2181";
            }

            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            this.client = CuratorFrameworkFactory.newClient(discoveryAddress, retryPolicy);
            this.client.start();

            serviceDiscovery = ServiceDiscoveryBuilder.builder(ZookeeperInstance.class).client(client).basePath(PATH).build();
            serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registry(NodeMetaInfo info) {
        try {

            ZookeeperInstance zInstance = new ZookeeperInstance(info.getMetaData());

            ServiceInstanceBuilder builder = builder()
                    .id(info.getId())
                    .name(info.getServiceName())
                    .address(info.getIp())
                    .port(info.getPort())
                    .payload(zInstance);

            serviceDiscovery.registerService(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<NodeMetaInfo> list(String name) {
        Set<NodeMetaInfo> result = new HashSet<>();
        try {
            Collection<ServiceInstance<ZookeeperInstance>> serviceInstances = serviceDiscovery.queryForInstances(name);
            for (ServiceInstance<ZookeeperInstance> serviceInstance : serviceInstances) {
                NodeMetaInfo metaInfo = new NodeMetaInfo();
                metaInfo.setIp(serviceInstance.getAddress());
                metaInfo.setPort(serviceInstance.getPort());
                metaInfo.setServiceName(serviceInstance.getName());
                metaInfo.setPayload(serviceInstance.getPayload().getMetadata());
                result.add(metaInfo);
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

}
