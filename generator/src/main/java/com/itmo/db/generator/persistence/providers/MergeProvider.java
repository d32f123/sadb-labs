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
        basePackages = "com.itmo.db.generator.persistence.db.merge",
        entityManagerFactoryRef = "oracleEntityManager",
        transactionManagerRef = "oracleTransactionManager"
)
@EntityScan(basePackages = "com.itmo.db.generator.model.entity.*")
public class MergeProvider {
    @Autowired
    private final Environment env;

    public MergeProvider(@Autowired Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource oracleDataSource() {
        var oracleDataSource = ProviderHelper.getDataSource("merge.", env);
        oracleDataSource.setSchema(env.getProperty("merge.spring.datasource.schema"));
        return oracleDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManager(
            @Autowired @Qualifier("mergeDataSource") DataSource dataSource
    ) {
        return ProviderHelper.getEntityManager("merge.", env, dataSource, "com.itmo.db.generator.persistence.db.merge");
    }

    @Bean
    public PlatformTransactionManager oracleTransactionManager(
            @Autowired @Qualifier("oracleEntityManager") LocalContainerEntityManagerFactoryBean entityManager
    ) {
        return ProviderHelper.getTransactionManager(entityManager);
    }
}
