package org.xxz.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;


/**
 * @author jsbxyyx
 * @since
 */
@Configuration
public class Config implements ApplicationContextAware {

    private ApplicationContext ac;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Primary
    @Bean("mysqlds")
    public DruidDataSource mysqlds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        Environment env = ac.getEnvironment();
        druidDataSource.setUrl(env.getProperty("spring.datasource.mysql.url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource.mysql.username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource.mysql.password"));
        return druidDataSource;
    }

    @Bean("mysqldsp")
    public DataSourceProxy mysqldsp(@Autowired @Qualifier("mysqlds") DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    @Primary
    @Bean("mysqljdbcTemplate")
    public JdbcTemplate mysqljdbcTemplate(@Autowired @Qualifier("mysqldsp") DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Primary
    @Bean("mysqlnamedJdbcTemplate")
    public NamedParameterJdbcTemplate mysqlnamedJdbcTemplate(@Autowired @Qualifier("mysqljdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean("oracleds")
    public DruidDataSource oracleds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        Environment env = ac.getEnvironment();
        druidDataSource.setUrl(env.getProperty("spring.datasource.oracle.url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource.oracle.username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource.oracle.password"));
        return druidDataSource;
    }

    @Bean("oracledsp")
    public DataSourceProxy oracledsp(@Autowired @Qualifier("oracleds") DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    @Bean("oraclejdbcTemplate")
    public JdbcTemplate oraclejdbcTemplate(@Autowired @Qualifier("oracledsp") DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean("oraclenamedJdbcTemplate")
    public NamedParameterJdbcTemplate oraclenamedJdbcTemplate(@Autowired @Qualifier("oraclejdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean("postgresqlds")
    public DruidDataSource postgresqlds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        Environment env = ac.getEnvironment();
        druidDataSource.setUrl(env.getProperty("spring.datasource.postgresql.url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource.postgresql.username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource.postgresql.password"));
        return druidDataSource;
    }

    @Bean("postgresqldsp")
    public DataSourceProxy postgresqldsp(@Autowired @Qualifier("postgresqlds") DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    @Bean("postgresqljdbcTemplate")
    public JdbcTemplate postgresqljdbcTemplate(@Autowired @Qualifier("postgresqldsp") DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean("postgresqlnamedJdbcTemplate")
    public NamedParameterJdbcTemplate postgresqlnamedJdbcTemplate(@Autowired @Qualifier("postgresqljdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }
}
