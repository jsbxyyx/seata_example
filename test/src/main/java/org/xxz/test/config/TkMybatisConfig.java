package org.xxz.test.config;

import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@AutoConfigureAfter(Config.class)
public class TkMybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        factoryBean.setConfiguration(mybatisConfig());
        return factoryBean.getObject();
    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSourceProxy) throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSourceProxy);
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath*:/mapper/*.xml"));
//        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
//        factoryBean.setConfiguration(mybatisConfig());
//        return factoryBean.getObject();
//    }

    @Bean
    public org.mybatis.spring.mapper.MapperScannerConfigurer mapperScannerConfigurer() {
        org.mybatis.spring.mapper.MapperScannerConfigurer conf = new org.mybatis.spring.mapper.MapperScannerConfigurer();
        conf.setBasePackage("org.xxz.test.dao");
        conf.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return conf;
    }

    private tk.mybatis.mapper.session.Configuration mybatisConfig() {
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        Properties properties = new Properties();
        properties.setProperty("notEmpty", "true");
        configuration.setMapperProperties(properties);
        return configuration;
    }

}
