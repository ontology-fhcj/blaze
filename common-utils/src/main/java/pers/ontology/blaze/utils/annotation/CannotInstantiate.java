package pers.ontology.blaze.utils.annotation;

import java.lang.annotation.*;

/**
 * <h3>不能实例化标记</h3>
 *
 * <p>标记了该注解的通常不参与反射阶段的代码运行,或者不能实例化
 *
 * @author ontology
 * @since 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CannotInstantiate {}
