package org.xxz.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author
 * @since
 */
@ConditionalOnProperty(value = "spring.datasource.mysql8.enable", havingValue = "true")
@Configuration
public class MySQL8Config implements ApplicationContextAware {

    private ApplicationContext ac;

    @Primary
    @Bean("mysql8ds")
    public DataSource mysql8ds() {
//        DruidDataSource dataSource = new DruidDataSource();
        HikariDataSource dataSource = new HikariDataSource();
        String ds = "mysql8";
        Environment env = ac.getEnvironment();
        dataSource.setJdbcUrl(env.getProperty("spring.datasource." + ds + ".url"));
        dataSource.setUsername(env.getProperty("spring.datasource." + ds + ".username"));
        dataSource.setPassword(env.getProperty("spring.datasource." + ds + ".password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource." + ds + ".driver-class-name"));
        return dataSource;
    }

    @Bean("mysql8dsp")
    public DataSourceProxy mysql8dsp(@Autowired @Qualifier("mysql8ds") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Primary
    @Bean("mysql8jdbcTemplate")
    public JdbcTemplate mysql8jdbcTemplate(@Autowired @Qualifier("mysql8dsp") DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean("mysql8jdbcTemplateorigin")
    public JdbcTemplate mysql8jdbcTemplateorigin(@Autowired @Qualifier("mysql8ds") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean("mysql8namedJdbcTemplate")
    public NamedParameterJdbcTemplate mysql8namedJdbcTemplate(@Autowired @Qualifier("mysql8jdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Primary
    @Bean("mysql8TM")
    public DataSourceTransactionManager mysql8DataSourceTransactionManager(@Autowired @Qualifier("mysql8dsp") DataSourceProxy dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

}
