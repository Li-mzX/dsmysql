package com.limz.mysql.dsmysql.Filter.DataBaseShard;


/**
 * 数据分表异常类
 */
public class ShardException extends RuntimeException{

    public ShardException(String msg){
        super(msg);
    }
}
