package org.csp.rpc.bootstrap.config.spring.configuration;

import org.csp.rpc.bootstrap.AbstractConfig;
import org.csp.rpc.bootstrap.ConfigManager;
import org.csp.rpc.bootstrap.MetaDataConfig;
import org.csp.rpc.bootstrap.RegistryConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;

public class RpcConfigurationRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, Ordered {

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        // registry RegistryConfig
        generatorConfigBeanDefinition(registry, RegistryConfig.class, ConfigManager.ConfigKeyEnum.REGISTRY.getConfigPrefix());

        // registry metaData
        generatorConfigBeanDefinition(registry, MetaDataConfig.class, ConfigManager.ConfigKeyEnum.META_DATA.getConfigPrefix());
    }

    private void generatorConfigBeanDefinition(BeanDefinitionRegistry registry, Class<? extends AbstractConfig> config, String prefixKey) {
        Field[] fields = config.getDeclaredFields();
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(config)
                .setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        for (Field field : fields) {
            String fieldName = field.getName();
            String key = prefixKey + "." + fieldName;
            builder.addPropertyValue(fieldName, environment.getProperty(key, ""));
        }
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        // full qualified name
        registry.registerBeanDefinition(config.getName(), beanDefinition);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
