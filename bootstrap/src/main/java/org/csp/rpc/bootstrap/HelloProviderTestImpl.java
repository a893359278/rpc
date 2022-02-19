package org.csp.rpc.bootstrap;

@Service(timeout = 123, retires = 121, weight = 1212)
public class HelloProviderTestImpl implements HelloProviderTest {
    @Override
    public String hello(String content) {
        return "hello, i am kangkang!";
    }
}
