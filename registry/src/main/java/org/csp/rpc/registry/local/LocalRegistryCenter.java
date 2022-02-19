//package org.csp.rpc.registry.local;
//
//import cn.hutool.core.util.StrUtil;
//import org.csp.rpc.registry.api.NodeMetaInfo;
//import org.csp.rpc.registry.api.RegistryCenter;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class LocalRegistryCenter implements RegistryCenter {
//
//    private ConcurrentHashMap<String /*app name*/, Set<NodeMetaInfo>> registryMap = new ConcurrentHashMap<>();
//
//    @Override
//    public void connect(String discoveryAddress) {
//        // do no thing
//    }
//
//    @Override
//    public void registry(NodeMetaInfo info) {
//        if (Objects.isNull(info)) {
//            return;
//        }
//
//        if (registryMap.containsKey(info.getId())) {
//            registryMap.get(info.getId()).add(info);
//        } else {
//            Set<NodeMetaInfo> list = new HashSet<>();
//            list.add(info);
//            registryMap.put(info.getId(), list);
//        }
//    }
//
//    @Override
//    public Set<NodeMetaInfo> list(String name) {
//        return registryMap.get(name);
//    }
//
//}
