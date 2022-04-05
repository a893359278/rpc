//package org.csp.rpc.bootstrap.config.spring.context;
//
//import com.alibaba.fastjson.JSON;
//import org.csp.rpc.example.HelloProviderTest;
//import org.csp.rpc.common.NetUtils;
//import org.csp.rpc.registry.api.Message;
//import org.csp.rpc.registry.api.NodeMetaInfo;
//import org.csp.rpc.registry.zk.ZooKeeperRegistryCenter;
//import org.csp.rpc.remoting.netty.NettyClientConfig;
//import org.csp.rpc.remoting.netty.NettyRemotingClient;
//import org.csp.rpc.remoting.netty.NettyRemotingServer;
//import org.csp.rpc.remoting.netty.NettyServerConfig;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//
//import java.lang.reflect.Method;
//import java.net.InetAddress;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//public class ServiceBeanPostProcessor implements BeanPostProcessor {
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
//
//    public static void main(String[] args) {
//
//        // 启动进程
//        NettyRemotingServer server = new NettyRemotingServer(new NettyServerConfig());
//        server.start();
//
//        System.out.println("111");
//
//        // 向 zk 注册
//        Class cls = HelloProviderTest.class;
//        InetAddress localAddress = NetUtils.getLocalAddress();
//        ZooKeeperRegistryCenter registryCenter = new ZooKeeperRegistryCenter();
//
//        NodeMetaInfo metaInfo = new NodeMetaInfo();
//        metaInfo.setIp(localAddress.getHostAddress());
//        // todo rpc port
//        metaInfo.setPort(28880);
//
//        Map<String, String> metadata = new HashMap<>();
//
//        Set<String> methods = new HashSet<>();
//        for (Method method : cls.getMethods()) {
//            methods.add(method.getName());
//        }
//
//        metadata.put("methods", JSON.toJSONString(methods));
//        metaInfo.setPayload(metadata);
//
//        metaInfo.setServiceName(cls.getName());
//        registryCenter.registry(metaInfo);
//
//        Set<NodeMetaInfo> list = registryCenter.list(cls.getName());
//        for (NodeMetaInfo info : list) {
//
//            NettyClientConfig config = new NettyClientConfig();
//            config.setPort(info.getPort());
//            config.setIp(info.getIp());
//
//            NettyRemotingClient client = new NettyRemotingClient(config);
//            client.connect();
//
//            Map<String, String> map = new HashMap<>();
//            map.put("key4", "value1");
//            map.put("key1", "value2");
//            map.put("key2", "value3");
//            map.put("key3", "value4");
//
//            Message message = new Message();
//            message.setMsg(map);
//            client.getChannel().writeAndFlush(message);
//        }
//
//
//    }
//
//
//}
