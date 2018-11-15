package com.limz.mysql.dsmysql.Entity;


import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class User implements Serializable, ShardEntity {



    private String userId;

    @NotNull(message = "用户名不能为空")
    private String userName;

    private String msg;

    private List<Telephone> telephones;

    public String getShardName(){
        return userId != null ? userId.substring(0,2) : null;
    }
}
