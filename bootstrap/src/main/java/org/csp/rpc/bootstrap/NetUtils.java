package org.csp.rpc.bootstrap;

import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.util.Collections.emptyList;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * 源码来自 dubbo NetUtils
 */
public class NetUtils {

    private static volatile InetAddress LOCAL_ADDRESS = null;

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    static String ANYHOST_VALUE = "0.0.0.0";
    static String LOCALHOST_VALUE = "127.0.0.1";

    private static String RPC_PREFERRED_NETWORK_INTERFACE = "rpc.network.interface.preferred";
    private static String RPC_NETWORK_IGNORED_INTERFACE = "rpc.network.interface.ignored";

    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            NetworkInterface networkInterface = findNetworkInterface();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                Optional<InetAddress> addressOp = toValidAddress(addresses.nextElement());
                if (addressOp.isPresent()) {
                    try {
                        if (addressOp.get().isReachable(100)) {
                            return addressOp.get();
                        }
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
//            logger.warn(e);
        }

        try {
            localAddress = InetAddress.getLocalHost();
            Optional<InetAddress> addressOp = toValidAddress(localAddress);
            if (addressOp.isPresent()) {
                return addressOp.get();
            }
        } catch (Throwable e) {
            e.printStackTrace();
//            logger.warn(e);
        }


        return localAddress;
    }


    public static NetworkInterface findNetworkInterface() {

        List<NetworkInterface> validNetworkInterfaces = emptyList();
        try {
            validNetworkInterfaces = getValidNetworkInterfaces();
        } catch (Throwable e) {
            e.printStackTrace();
//            logger.warn(e);
        }

        NetworkInterface result = null;

        // Try to find the preferred one
        for (NetworkInterface networkInterface : validNetworkInterfaces) {
            if (isPreferredNetworkInterface(networkInterface)) {
                result = networkInterface;
                break;
            }
        }

        if (result == null) { // If not found, try to get the first one
            for (NetworkInterface networkInterface : validNetworkInterfaces) {
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    Optional<InetAddress> addressOp = toValidAddress(addresses.nextElement());
                    if (addressOp.isPresent()) {
                        try {
                            if (addressOp.get().isReachable(100)) {
                                return networkInterface;
                            }
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        }

        if (result == null) {
            result = first(validNetworkInterfaces);
        }

        return result;
    }

    private static List<NetworkInterface> getValidNetworkInterfaces() throws SocketException {
        List<NetworkInterface> validNetworkInterfaces = new LinkedList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (ignoreNetworkInterface(networkInterface)) { // ignore
                continue;
            }
            validNetworkInterfaces.add(networkInterface);
        }
        return validNetworkInterfaces;
    }

    private static boolean ignoreNetworkInterface(NetworkInterface networkInterface) throws SocketException {
        if (networkInterface == null
                || networkInterface.isLoopback()
                || networkInterface.isVirtual()
                || !networkInterface.isUp()){
            return true;
        }
        String ignoredInterfaces = System.getProperty(RPC_NETWORK_IGNORED_INTERFACE);
        String networkInterfaceDisplayName;
        if(StrUtil.isNotEmpty(ignoredInterfaces)
                &&StrUtil.isNotEmpty(networkInterfaceDisplayName=networkInterface.getDisplayName())){
            for(String ignoredInterface: ignoredInterfaces.split(",")){
                String trimIgnoredInterface = ignoredInterface.trim();
                boolean matched = false;
                try {
                    matched = networkInterfaceDisplayName.matches(trimIgnoredInterface);
                } catch (PatternSyntaxException e) {
                    // if trimIgnoredInterface is a invalid regular expression, a PatternSyntaxException will be thrown out
                    e.printStackTrace();
//                    logger.warn("exception occurred: " + networkInterfaceDisplayName + " matches " + trimIgnoredInterface, e);
                } finally {
                    if (matched) {
                        return true;
                    }
                    if (networkInterfaceDisplayName.equals(trimIgnoredInterface)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isPreferredNetworkInterface(NetworkInterface networkInterface) {
        String preferredNetworkInterface = System.getProperty(RPC_PREFERRED_NETWORK_INTERFACE);
        return Objects.equals(networkInterface.getDisplayName(), preferredNetworkInterface);
    }

    private static Optional<InetAddress> toValidAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            Inet6Address v6Address = (Inet6Address) address;
            if (isPreferIPV6Address()) {
                return Optional.ofNullable(normalizeV6Address(v6Address));
            }
        }
        if (isValidV4Address(address)) {
            return Optional.of(address);
        }
        return Optional.empty();
    }

    static boolean isPreferIPV6Address() {
        return Boolean.getBoolean("java.net.preferIPv6Addresses");
    }

    static InetAddress normalizeV6Address(Inet6Address address) {
        String addr = address.getHostAddress();
        int i = addr.lastIndexOf('%');
        if (i > 0) {
            try {
                return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
            } catch (UnknownHostException e) {
                // ignore
                e.printStackTrace();
            }
        }
        return address;
    }

    static boolean isValidV4Address(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }

        String name = address.getHostAddress();
        return (name != null
                && IP_PATTERN.matcher(name).matches()
                && !ANYHOST_VALUE.equals(name)
                && !LOCALHOST_VALUE.equals(name));
    }

    private static <T> T first(Collection<T> values) {
        if (isEmpty(values)) {
            return null;
        }
        if (values instanceof List) {
            List<T> list = (List<T>) values;
            return list.get(0);
        } else {
            return values.iterator().next();
        }
    }
}
