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
@ConditionalOnProperty(value = "spring.datasource.postgresql.enable", havingValue = "true")
@Configuration
public class PostgresqlConfig implements ApplicationContextAware {

    private ApplicationContext ac;

    @Bean("postgresqlds")
    public DataSource postgresqlds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        setDruidDataSourceProperties(druidDataSource, "postgresql");
        return druidDataSource;
    }

    @Bean("postgresqldsp")
    public DataSourceProxy postgresqldsp(@Autowired @Qualifier("postgresqlds") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
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
