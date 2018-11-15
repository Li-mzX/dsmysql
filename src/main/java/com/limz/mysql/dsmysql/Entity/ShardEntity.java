package com.limz.mysql.dsmysql.Entity;


/**
 * 需要分表的实体类，必须实现的接口
 */
public interface ShardEntity {

    String getShardName();
}
