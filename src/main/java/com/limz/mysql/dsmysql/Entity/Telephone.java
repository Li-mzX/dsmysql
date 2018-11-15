package com.limz.mysql.dsmysql.Entity;

import lombok.Data;
import java.io.Serializable;


@Data
public class Telephone implements Serializable, ShardEntity{

    private Long id;

    private String userId;

    private String telephone;

    public String getShardName(){
        return userId != null ? userId.substring(0,2) : null;
    }
}
