package org.xxz.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author
 * @since
 */
@ConditionalOnProperty(value = "spring.datasource.mysql.enable", havingValue = "true")
@Configuration
public class MySQLConfig implements ApplicationContextAware {

    private ApplicationContext ac;

    @Bean("mysqlds")
    public DataSource mysqlds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        setDruidDataSourceProperties(druidDataSource, "mysql");
        return druidDataSource;
    }

    @Bean("mysqldsp")
    public DataSourceProxy mysqldsp(@Autowired @Qualifier("mysqlds") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean("mysqljdbcTemplate")
    public JdbcTemplate mysqljdbcTemplate(@Autowired @Qualifier("mysqldsp") DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean("mysqljdbcTemplateorigin")
    public JdbcTemplate mysqljdbcTemplateorigin(@Autowired @Qualifier("mysqlds") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("mysqlnamedJdbcTemplate")
    public NamedParameterJdbcTemplate mysqlnamedJdbcTemplate(@Autowired @Qualifier("mysqljdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean("mysqlTM")
    public DataSourceTransactionManager mysqlDataSourceTransactionManager(@Autowired @Qualifier("mysqldsp") DataSourceProxy dataSourceProxy) {
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

}
