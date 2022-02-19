package org.csp.rpc.bootstrap.config.spring.context;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class RpcComponentScanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        Set<String> basePackages = getPackages(annotationMetadata);

        registryRpcServiceBean(registry, basePackages);
    }

    private void registryRpcServiceBean(BeanDefinitionRegistry registry, Set<String> basePackages) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(RpcServiceRegistryPostProcessor.class);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        builder.addConstructorArgValue(basePackages);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

    private Set<String> getPackages(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(RpcComponentScan.class.getName()));
        String[] basePackages = attributes.getStringArray("basePackages");

        Set<String> packages = new LinkedHashSet<>(Arrays.asList(basePackages));

        if (CollectionUtil.isEmpty(packages)) {
            return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        }

        return packages;
    }
}
