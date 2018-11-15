package com.limz.mysql.dsmysql.Filter.DataBaseShard;

import com.limz.mysql.dsmysql.Entity.ShardEntity;
import net.sf.jsqlparser.schema.Table;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_FACTORY;
import static org.apache.ibatis.reflection.SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY;

/**
 * 分表查询 拦截器  核心功能
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class TableSegInterceptor implements Interceptor {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //SQL解析工厂
    private final SqlParserFactory parserFactory = new JSqlParserFactory();

    //sql语句存储字段
    private final Field boundSqlField;


    public TableSegInterceptor() {
        try {
            boundSqlField = BoundSql.class.getDeclaredField("sql");
            boundSqlField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (invocation.getTarget() instanceof Executor) {
            return invocation.proceed();
        }

        System.out.println("进入拦截器：====================");

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        MetaObject mo = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) mo.getValue("delegate.mappedStatement");

        //解析出MappedStatement的ID 从中获取Dao类信息
        String id = mappedStatement.getId();
        String clzName = id.substring(0,id.lastIndexOf("."));
        Class<?> clzObj = Class.forName(clzName);
        //是否添加 @TableShard注解
        TableShard ts = clzObj.getAnnotation(TableShard.class);
        if (ts != null && ts.split()){

            // 进行SQL解析，如果未找到表名，则跳过
            BoundSql boundSql = statementHandler.getBoundSql();
            SqlParser sqlParser = parserFactory.createParser(boundSql.getSql());
            List<Table> tables = sqlParser.getTables();
            if (tables.isEmpty()) {
                return invocation.proceed();
            }

            //获取分表后缀名
            String shardName = null;
            Object v2 = mo.getValue("delegate.boundSql.parameterObject");
            if (v2 instanceof Map){
                Map pm = (Map) v2;
                //一定先从参数中查询，是否有 @Param("shardName") 的参数， 如果有，当做分表后缀，
                // 如果没有， 将遍历参数， 找到实现了ShardEntity接口的参数
                shardName = (String) pm.get("shardName");
                if (shardName == null){
                    Collection values = pm.values();
                    for (Object o : values) {
                        if (o instanceof ShardEntity){
                            ShardEntity se = (ShardEntity) o;
                            shardName = se.getShardName();
                            break;
                        }
                    }
                }
                //如果只有一个参数，为实体类，则直接从中获取属性
            }else {
                if (v2 instanceof ShardEntity) {
                    ShardEntity se = (ShardEntity) v2;
                    shardName = se.getShardName();
                }
            }
            //如果参数中 未包含 shardName 相关参数， 则抛出异常
            if (shardName == null)
                throw new ShardException("shardName must be not empty!");


            // 设置实际的表名
            for (int index = 0; index < tables.size(); index++) {
                Table table = tables.get(index);
                //替换所有表名，为表名添加后缀
                String targetName = table.getName() + "_" + shardName;
                logger.info("Sharding table, {}-->{}", table, targetName);
                table.setName(targetName);
            }

            // 修改实际的SQL
            String targetSQL = sqlParser.toSQL();
            boundSqlField.set(boundSql, targetSQL);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
