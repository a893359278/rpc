package org.csp.rpc.bootstrap;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextTest {

    @Test
    public void componentScanTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(TestConfig.class);
        context.refresh();
        Object bean = context.getBean(RegistryConfig.class.getName());
        System.out.println(bean);

        Object bean1 = context.getBean("helloProviderTestImpl");
        System.out.println(bean1);

        Object bean2 = context.getBean("helloProviderTestImplServiceConfig");
        System.out.println(bean2);

    }
}
