package com.itmo.db.generator.persistence.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:/oracle-provider.properties"})
@EnableJpaRepositories(
        basePackages = "com.itmo.db.generator.persistence.db.oracle",
        entityManagerFactoryRef = "oracleEntityManager",
        transactionManagerRef = "oracleTransactionManager"
)
@EntityScan(basePackages = "com.itmo.db.generator.persistence.db.oracle.dao.*")
public class OracleProvider {
    @Autowired
    private final Environment env;

    public OracleProvider(@Autowired Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource oracleDataSource() {
        var oracleDataSource = ProviderHelper.getDataSource("oracle.", env);
        oracleDataSource.setSchema(env.getProperty("oracle.spring.datasource.schema"));
        return oracleDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManager(
            @Autowired @Qualifier("oracleDataSource") DataSource dataSource
    ) {
        return ProviderHelper.getEntityManager("oracle.", env, dataSource, "com.itmo.db.generator.persistence.db.oracle");
    }

    @Bean
    public PlatformTransactionManager oracleTransactionManager(
            @Autowired @Qualifier("oracleEntityManager") LocalContainerEntityManagerFactoryBean entityManager
    ) {
        return ProviderHelper.getTransactionManager(entityManager);
    }
}
