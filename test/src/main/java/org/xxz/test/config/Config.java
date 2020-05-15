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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;


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
        setDruidDataSourceProperties(druidDataSource, "mysql");
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

    @Bean("mysqljdbcTemplateorigin")
    public JdbcTemplate mysqljdbcTemplateorigin(@Autowired @Qualifier("mysqlds") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean("mysqlnamedJdbcTemplate")
    public NamedParameterJdbcTemplate mysqlnamedJdbcTemplate(@Autowired @Qualifier("mysqljdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Primary
    @Bean("mysqlTM")
    public DataSourceTransactionManager mysqlDataSourceTransactionManager(@Autowired @Qualifier("mysqldsp") DataSourceProxy dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }

    @Bean("oracleds")
    public DruidDataSource oracleds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        setDruidDataSourceProperties(druidDataSource, "oracle");
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

    @Bean("oraclejdbcTemplateorigin")
    public JdbcTemplate oraclejdbcTemplateorigin(@Autowired @Qualifier("oracleds") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("oraclenamedJdbcTemplate")
    public NamedParameterJdbcTemplate oraclenamedJdbcTemplate(@Autowired @Qualifier("oraclejdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean("oracleTM")
    public DataSourceTransactionManager oracleDataSourceTransactionManager(@Autowired @Qualifier("oracledsp") DataSourceProxy dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }

    @Bean("postgresqlds")
    public DruidDataSource postgresqlds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        setDruidDataSourceProperties(druidDataSource, "postgresql");
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

    @Bean("postgresqljdbcTemplateorigin")
    public JdbcTemplate postgresqljdbcTemplateorigin(@Autowired @Qualifier("postgresqlds") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("postgresqlnamedJdbcTemplate")
    public NamedParameterJdbcTemplate postgresqlnamedJdbcTemplate(@Autowired @Qualifier("postgresqljdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean("postgresqlTM")
    public DataSourceTransactionManager postgresqlDataSourceTransactionManager(@Autowired @Qualifier("postgresqldsp") DataSourceProxy dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }

    @Bean("mysql8ds")
    public DruidDataSource mysql8ds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        setDruidDataSourceProperties(druidDataSource, "mysql8");
        return druidDataSource;
    }

    @Bean("mysql8dsp")
    public DataSourceProxy mysql8dsp(@Autowired @Qualifier("mysql8ds") DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    @Bean("mysql8jdbcTemplate")
    public JdbcTemplate mysql8jdbcTemplate(@Autowired @Qualifier("mysql8dsp") DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean("mysql8jdbcTemplateorigin")
    public JdbcTemplate mysql8jdbcTemplateorigin(@Autowired @Qualifier("mysql8ds") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("mysql8namedJdbcTemplate")
    public NamedParameterJdbcTemplate mysql8namedJdbcTemplate(@Autowired @Qualifier("mysql8jdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean("mysql8TM")
    public DataSourceTransactionManager mysql8DataSourceTransactionManager(@Autowired @Qualifier("mysql8dsp") DataSourceProxy dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    private void setDruidDataSourceProperties(DruidDataSource druidDataSource, String ds) {
        Environment env = ac.getEnvironment();
        druidDataSource.setUrl(env.getProperty("spring.datasource." + ds + ".url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource." + ds + ".username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource." + ds + ".password"));
        druidDataSource.setDriverClassName(env.getProperty("spring.datasource." + ds + ".driver-class-name"));
    }

    // only 1.0.0
//    @Bean
//    public GlobalTransactionScanner globalTransactionScanner() {
//        return new GlobalTransactionScanner("my_test_tx_group");
//    }
}
