package org.xxz.test.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.BeansException;
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
    @Bean
    public DruidDataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        Environment env = ac.getEnvironment();
        druidDataSource.setUrl(env.getProperty("spring.datasource.url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource.username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource.password"));
        return druidDataSource;
    }

    @Bean
    public DataSourceProxy dataSource(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSourceProxy dataSourceProxy) {
        return new JdbcTemplate(dataSourceProxy);
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }
}
