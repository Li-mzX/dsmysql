package com.limz.mysql.dsmysql.Filter.DataBaseShard;


import java.io.StringReader;
import java.sql.SQLException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

/**
 * 使用JSql实现的SQL解析器工厂
 */
public class JSqlParserFactory implements SqlParserFactory {

    private final CCJSqlParserManager manager;

    public JSqlParserFactory() {
        manager = new CCJSqlParserManager();
    }

    public SqlParser createParser(String originalSql) throws SQLException {
        try {
            Statement statement = manager.parse(new StringReader(originalSql));
            SqlParser parse = new SqlParser(statement);
            parse.init();
            return parse;
        } catch (JSQLParserException e) {
            throw new SQLException("SQL Parse Failed", e);
        }

    }
}
