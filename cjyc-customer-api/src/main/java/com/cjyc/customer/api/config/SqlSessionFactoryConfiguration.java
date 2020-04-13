package com.cjyc.customer.api.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.cjyc.common.model.interceptor.MapV2KInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class SqlSessionFactoryConfiguration {

    @Bean("sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        //SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        // 读取mapper 配置文件
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml");
        factoryBean.setMapperLocations(resources);
        // 加入SQL 语句执行拦截器
        factoryBean.setPlugins(new Interceptor[] {new MapV2KInterceptor()});
        return factoryBean.getObject();
    }
}
