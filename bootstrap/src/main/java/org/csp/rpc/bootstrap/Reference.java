package org.csp.rpc.bootstrap;

import java.lang.annotation.*;

/**
 * 服务引用者
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {
}
