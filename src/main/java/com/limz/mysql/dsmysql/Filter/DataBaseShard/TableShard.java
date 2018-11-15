package com.limz.mysql.dsmysql.Filter.DataBaseShard;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要分表的 Dao 添加此注解，标记为需要分表
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableShard {

    //默认分表， 为false时， 此注解无效
    boolean split() default true;
}


//睡醒之后，使用sql语句工厂的方式再试试