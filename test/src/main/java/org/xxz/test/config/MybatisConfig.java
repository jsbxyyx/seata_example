package org.xxz.test.config;

import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Properties;

@Configuration
@AutoConfigureAfter(Config.class)
public class MybatisConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Primary
    @Bean("mysqlsqlSessionFactory")
    public SqlSessionFactory mysqlsqlSessionFactory(@Autowired @Qualifier("mysqldsp") DataSourceProxy dataSourceProxy) throws Exception {
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

    @Bean("oraclesqlSessionFactory")
    public SqlSessionFactory oraclesqlSessionFactory(@Autowired @Qualifier("oracledsp") DataSourceProxy dataSourceProxy) throws Exception {
        // tk mybatis
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setConfiguration(mybatisConfig());

        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        return factoryBean.getObject();
    }

    @Bean("postgresqlsqlSessionFactory")
    public SqlSessionFactory postgresqlsqlSessionFactory(@Autowired @Qualifier("postgresqldsp") DataSourceProxy dataSourceProxy) throws Exception {
        // tk mybatis
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setConfiguration(mybatisConfig());

        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/*.xml"));
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        return factoryBean.getObject();
    }

    @Bean("mysql8sqlSessionFactory")
    public SqlSessionFactory mysql8sqlSessionFactory(@Autowired @Qualifier("mysql8dsp") DataSourceProxy dataSourceProxy) throws Exception {
        // tk mybatis
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setConfiguration(mybatisConfig());

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

    @Bean("mysqlmapperScannerConfigurer")
    public org.mybatis.spring.mapper.MapperScannerConfigurer mysqlmapperScannerConfigurer() {
        org.mybatis.spring.mapper.MapperScannerConfigurer conf = new org.mybatis.spring.mapper.MapperScannerConfigurer();
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        conf.setBasePackage(basePackage);
        conf.setSqlSessionFactoryBeanName("mysqlsqlSessionFactory");
        return conf;
    }

    @Bean("oraclemapperScannerConfigurer")
    public org.mybatis.spring.mapper.MapperScannerConfigurer oraclemapperScannerConfigurer() {
        org.mybatis.spring.mapper.MapperScannerConfigurer conf = new org.mybatis.spring.mapper.MapperScannerConfigurer();
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        conf.setBasePackage(basePackage);
        conf.setSqlSessionFactoryBeanName("oraclesqlSessionFactory");
        return conf;
    }

    @Bean("postgresqlmapperScannerConfigurer")
    public org.mybatis.spring.mapper.MapperScannerConfigurer postgresqlmapperScannerConfigurer() {
        org.mybatis.spring.mapper.MapperScannerConfigurer conf = new org.mybatis.spring.mapper.MapperScannerConfigurer();
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        conf.setBasePackage(basePackage);
        conf.setSqlSessionFactoryBeanName("postgresqlsqlSessionFactory");
        return conf;
    }

    @Bean("mysql8mapperScannerConfigurer")
    public org.mybatis.spring.mapper.MapperScannerConfigurer mysql8mapperScannerConfigurer() {
        org.mybatis.spring.mapper.MapperScannerConfigurer conf = new org.mybatis.spring.mapper.MapperScannerConfigurer();
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        conf.setBasePackage(basePackage);
        conf.setSqlSessionFactoryBeanName("mysql8sqlSessionFactory");
        return conf;
    }

    private tk.mybatis.mapper.session.Configuration mybatisConfig() {
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        configuration.addMappers(basePackage);
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
