package com.limz.mysql.dsmysql.Filter.DataBaseShard;


import java.sql.SQLException;

/**
 * SQL解析工厂
 */
public interface SqlParserFactory {


    SqlParser createParser(String originalSql) throws SQLException;
}
