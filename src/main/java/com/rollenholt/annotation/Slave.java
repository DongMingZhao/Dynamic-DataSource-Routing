package com.rollenholt.annotation;

import java.lang.annotation.*;

/**
 * Created by wenchao.ren
 * 2014/7/14.
 *
 * 对数据库从库操作的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Slave {

    /**
     * Spring配置的dataSource的Id
     */
    String value() default "";

    /**
     * failover机制，默认为true，Slave出现异常以后，会自动切换到Master上面
     * 如果设置为false，则slave出现异常以后，不会自动切换到Master上面
     */
    boolean failover() default true;

}
