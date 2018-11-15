package com.limz.mysql.dsmysql.Entity;


/**
 * 需要分表的实体类，必须实现的接口
 */
public interface ShardEntity {


    /**
     * 需要分表的类，需要实现此方法， 提供分表后缀名的获取
     * @return
     */
    String getShardName();
}
