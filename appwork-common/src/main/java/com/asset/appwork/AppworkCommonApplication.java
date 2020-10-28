package com.asset.appwork;

import com.asset.appwork.config.TenantManagement;
import com.asset.appwork.model.IdentityComponentsPerson;
import com.asset.appwork.util.ReflectionUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Optionals;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by karim on 10/19/20.
 */

@SpringBootApplication
public class AppworkCommonApplication {
    @Autowired
    private Environment env;

    public static void main(String[] args) {
        IdentityComponentsPerson identityComponentsPerson = new IdentityComponentsPerson();
        identityComponentsPerson.setDisplayName("Name");
        ReflectionUtil.of(identityComponentsPerson).ifPresent("getId", (s)-> {
            System.out.println(s);
        }).ifPresent("getDisplayName", (s)->{
            System.out.println(s);
        }).ifPresent("getTest", (s) -> {

        });
    }

    @Primary
    @Bean
    public DataSource dataSource() {

        AbstractRoutingDataSource dataSource = new TenantManagement.TenantAwareRoutingSource();

        Map<Object,Object> targetDataSources = new ConcurrentHashMap<Object, Object>();

        targetDataSources.put("TenantOne", tenantOne());

        dataSource.setTargetDataSources(targetDataSources);

        dataSource.afterPropertiesSet();

        return dataSource;
    }


    public DataSource tenantOne(){

        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.addDataSourceProperty("url", env.getProperty("spring.datasource.url"));
        dataSource.addDataSourceProperty("user", env.getProperty("spring.datasource.username"));
        dataSource.addDataSourceProperty("password", env.getProperty("spring.datasource.password"));
        dataSource.setInitializationFailTimeout(0);
        return dataSource;

    }



}
