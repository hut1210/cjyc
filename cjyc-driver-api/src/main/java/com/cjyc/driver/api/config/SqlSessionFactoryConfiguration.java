package com.cjyc.driver.api.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlSessionFactoryConfiguration {

    /*@Bean("sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        // 读取mapper 配置文件
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml");
        factoryBean.setMapperLocations(resources);
        // 加入SQL 语句执行拦截器
        factoryBean.setPlugins(new Interceptor[] {new MapV2KInterceptor()});
        return factoryBean.getObject();
    }*/
}
