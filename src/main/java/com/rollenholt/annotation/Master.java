package com.rollenholt.annotation;

import java.lang.annotation.*;

/**
 * Created by wenchao.ren
 * 2014/7/14.
 *
 * 对数据库主库操作的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Master {

}
