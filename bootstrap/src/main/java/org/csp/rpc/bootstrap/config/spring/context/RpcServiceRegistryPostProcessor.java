package org.csp.rpc.bootstrap.config.spring.context;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.csp.rpc.bootstrap.Service;
import org.csp.rpc.bootstrap.ServiceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class RpcServiceRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor,
        EnvironmentAware, ResourceLoaderAware, BeanClassLoaderAware {


    protected final Set<String> packages;

    private Environment environment;

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;


    public RpcServiceRegistryPostProcessor(Set<String> packages) {
        this.packages = packages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // registry listener, when context refresh start rpc application
        registryListener(beanDefinitionRegistry);

        // Resolve placeholder because placeholders may exist in variables.
        Set<String> resolvePackages = resolvePackages(packages);

        if (CollectionUtil.isNotEmpty(resolvePackages)) {
            registryServiceBean(beanDefinitionRegistry, resolvePackages);
        }
    }

    private void registryListener(BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(RpcBootstrapListener.class)
                .setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, beanDefinitionRegistry);
    }

    private void registryServiceBean(BeanDefinitionRegistry registry, Set<String> resolvePackages) {
        RpcClassPathBeanDefinitionScanner scanner = new RpcClassPathBeanDefinitionScanner(registry, environment, resourceLoader);

        BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);

        scanner.setBeanNameGenerator(beanNameGenerator);

        for (String path : resolvePackages) {
            scanner.scan(path);

            // find all @Service class, whether @ComponentScan scans or not.
            Set<BeanDefinitionHolder> beanDefinitionHolders = findServiceBeanDefinitionHolders(scanner, path, registry, beanNameGenerator);

            doRegistryServiceBean(beanDefinitionHolders, registry);
        }

    }

    private Set<BeanDefinitionHolder> findServiceBeanDefinitionHolders(RpcClassPathBeanDefinitionScanner scanner, String path, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
        Set<BeanDefinition> scanBeanDefinition = scanner.findCandidateComponents(path);

        Set<BeanDefinitionHolder> holders = new LinkedHashSet<>();

        for (BeanDefinition beanDefinition : scanBeanDefinition) {
            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
            holders.add(holder);
        }

        return holders;
    }

    private BeanNameGenerator resolveBeanNameGenerator(BeanDefinitionRegistry registry) {
        BeanNameGenerator beanNameGenerator = null;

        if (registry instanceof SingletonBeanRegistry) {
            SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry) registry;
            beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR);
        }

        if (Objects.isNull(beanNameGenerator)) {
            beanNameGenerator = new AnnotationBeanNameGenerator();
        }

        return beanNameGenerator;
    }

    private void doRegistryServiceBean(Set<BeanDefinitionHolder> scanBeanDefinition, BeanDefinitionRegistry registry) {
        if (CollectionUtil.isEmpty(scanBeanDefinition)) {
            return;
        }

        for (BeanDefinitionHolder beanDefinition : scanBeanDefinition) {

            Class<?> cls = resolveClassName(beanDefinition);

            AnnotationAttributes serviceAnnotationAttributes = getServiceAnnotationAttributes(cls);

            BeanDefinitionBuilder scBeanDefinitionBuilder = builderServiceConfig(cls, serviceAnnotationAttributes);

            String beanName = beanDefinition.getBeanName();

            String serviceBeanName = generatorServiceBeanName(beanName);

            // @Service convert to rpc ServiceConfig, avoid strong depends on spring.
            registry.registerBeanDefinition(serviceBeanName, Objects.requireNonNull(scBeanDefinitionBuilder.getBeanDefinition()));
        }
    }

    private String generatorServiceBeanName(String beanName) {
        return beanName + "ServiceConfig";
    }

    private BeanDefinitionBuilder builderServiceConfig(Class<?> beanClass, AnnotationAttributes serviceAnnotationAttributes) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServiceConfig.class);
        // resolve interfaceClass
        Class<?> interfaceClass = beanClass.isInterface() ? beanClass : beanClass.getInterfaces()[0];
        builder.addPropertyValue("reference", beanClass)
                .addPropertyValue("interfaceClass", interfaceClass)
                .addPropertyValue("interfaceName", interfaceClass.getName())
                .addPropertyValue("timeout", serviceAnnotationAttributes.getNumber("timeout"))
                .addPropertyValue("retires", serviceAnnotationAttributes.getNumber("retires"))
                .addPropertyValue("weight", serviceAnnotationAttributes.getNumber("weight"));

        return builder;
    }

    private AnnotationAttributes getServiceAnnotationAttributes(Class<?> cls) {
        Service service = Objects.requireNonNull(AnnotatedElementUtils.findMergedAnnotation(cls, Service.class));
        return AnnotationUtils.getAnnotationAttributes(service, false, false);
    }

    private Class<?> resolveClassName(BeanDefinitionHolder beanDefinition) {
        return ClassUtils.resolveClassName(Objects.requireNonNull(beanDefinition.getBeanDefinition().getBeanClassName()), classLoader);
    }

    private Set<String> resolvePackages(Set<String> packages) {
        Set<String> set = new LinkedHashSet<>(packages.size());
        for (String path : packages) {
            if (StrUtil.isNotBlank(path)) {
                set.add(environment.resolvePlaceholders(path));
            }
        }
        return set;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
