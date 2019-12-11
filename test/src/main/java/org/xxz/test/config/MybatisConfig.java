package org.xxz.test.config;

import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Properties;

@Configuration
@AutoConfigureAfter(Config.class)
public class MybatisConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy) throws Exception {
        // tk mybatis
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setConfiguration(mybatisConfig());

        // mybatis-plus
//        com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean factoryBean =
//            new com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean();

        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
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
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        conf.setBasePackage(basePackage);
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
