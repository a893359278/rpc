package org.csp.rpc.example.application.provider;

import org.csp.rpc.bootstrap.*;
import org.csp.rpc.example.api.HelloService;
import org.csp.rpc.remoting.netty.NettyRemotingServer;
import org.csp.rpc.remoting.netty.NettyServerConfig;

import java.io.IOException;

public class ProviderMain {
    public static void main(String[] args) throws IOException {
        ConfigManager.getInstance().addConfig("service",
                new ServiceConfig.ServiceConfigBuilder()
                .interfaceClass(HelloService.class)
                        .interfaceName("org.csp.rpc.example.api.HelloService")
                        .retires(2)
                        .timeout(3000)
                        .weight(1)
                        .builder());

        ConfigManager.getInstance().addConfig("registry",
                new RegistryConfig.RegistryConfigBuilder()
                        .host("localhost:2181")
                        .builder());

        ConfigManager.getInstance().addConfig("metaData",
                new MetaDataConfig.MetaDataConfigBuilder()
                .port(20011)
                .builder());

        RpcBootstrap.getInstance().start();

        NettyServerConfig config = new NettyServerConfig();
        config.setPort(20011);
        NettyRemotingServer server = new NettyRemotingServer(config);
        server.start();

        System.in.read();
    }
}
