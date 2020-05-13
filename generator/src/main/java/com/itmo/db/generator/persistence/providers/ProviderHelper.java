package com.itmo.db.generator.persistence.providers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class ProviderHelper {

    public static DataSource getDataSource(String prefix, Environment env) {
        log.debug("Url: '{}', username: '{}', password: '{}'",
                env.getProperty(prefix + "spring.datasource.url"),
                env.getProperty(prefix + "spring.datasource.username"),
                env.getProperty(prefix + "spring.datasource.password"));
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty(prefix + "spring.datasource.url"));
        dataSource.setUsername(env.getProperty(prefix + "spring.datasource.username"));
        dataSource.setPassword(env.getProperty(prefix + "spring.datasource.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty(prefix + "spring.datasource.driver-class-name")));

        return dataSource;
    }

    public static LocalContainerEntityManagerFactoryBean getEntityManager(String prefix,
                                                                          Environment env,
                                                                          DataSource dataSource,
                                                                          String packageName) {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(packageName);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        JpaProperties jpaProperties = new JpaProperties();
        jpaProperties.setGenerateDdl(Boolean.parseBoolean(env.getProperty(prefix + "spring.jpa.generate-ddl")));
        jpaProperties.setDatabasePlatform(env.getProperty(prefix + "spring.jpa.database-platform"));
        Properties properties = new Properties();
        jpaProperties.getProperties().forEach(properties::put);
        properties.put("hibernate.hbm2ddl.auto", env.getProperty(prefix + "spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty(prefix + "spring.jpa.database-platform"));
        em.setJpaProperties(properties);

        return em;
    }

    public static PlatformTransactionManager getTransactionManager(
            LocalContainerEntityManagerFactoryBean entityManager
    ) {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }


}
