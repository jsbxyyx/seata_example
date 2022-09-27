package org.xxz.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
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
    public DruidDataSource mysql8ds() {
        DruidDataSource druidDataSource = new DruidDataSource();
        setDruidDataSourceProperties(druidDataSource, "mysql8");
        return druidDataSource;
    }

    @Bean("mysql8dsp")
    public DataSourceProxy mysql8dsp(@Autowired @Qualifier("mysql8ds") DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
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

    private void setDruidDataSourceProperties(DruidDataSource druidDataSource, String ds) {
        Environment env = ac.getEnvironment();
        druidDataSource.setUrl(env.getProperty("spring.datasource." + ds + ".url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource." + ds + ".username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource." + ds + ".password"));
        druidDataSource.setDriverClassName(env.getProperty("spring.datasource." + ds + ".driver-class-name"));
    }

}
