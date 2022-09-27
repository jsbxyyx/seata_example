package org.xxz.test.config;

import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Properties;

/**
 * @author
 * @since
 */
@ConditionalOnProperty(value = "spring.datasource.postgresql.enable", havingValue = "true")
@Configuration
@AutoConfigureAfter(PostgresqlConfig.class)
public class MybatisPostgresqlConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

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

    @Bean("postgresqlmapperScannerConfigurer")
    public org.mybatis.spring.mapper.MapperScannerConfigurer postgresqlmapperScannerConfigurer() {
        org.mybatis.spring.mapper.MapperScannerConfigurer conf = new org.mybatis.spring.mapper.MapperScannerConfigurer();
        String basePackage = applicationContext.getEnvironment().getProperty("mybatis.mapper.basePackage");
        conf.setBasePackage(basePackage);
        conf.setSqlSessionFactoryBeanName("postgresqlsqlSessionFactory");
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
