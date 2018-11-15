package com.limz.mysql.dsmysql;

import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.limz.mysql.dsmysql.Filter.DataBaseShard.TableSegInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.limz.mysql.dsmysql.Dao")
public class DsmysqlApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(DsmysqlApplication.class, args);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){

        super.configureMessageConverters(converters);
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty
        );

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        StringHttpMessageConverter smc = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(smc);
        converters.add(fastConverter);
    }

    @Bean("logFilter")
    public LogFilter logFilter(){
        Slf4jLogFilter logFilter = new Slf4jLogFilter();
        logFilter.setConnectionLogEnabled(false);
        logFilter.setStatementLogEnabled(false);
        logFilter.setResultSetLogEnabled(true);
        logFilter.setStatementExecutableSqlLogEnable(true);
        return logFilter;
    }

    @Bean
    public Interceptor getInterceptor(){
        Interceptor interceptor = new TableSegInterceptor();
        return interceptor;
    }
}
