package org.csp.rpc.example.spring.provider;

import org.csp.rpc.bootstrap.Service;
import org.csp.rpc.example.api.HelloService;

@Service(timeout = 123, retires = 121, weight = 1212)
public class HelloProviderTestImpl implements HelloService {
    @Override
    public String hello(String content) {
        return "hello, i am kangkang!";
    }
}
