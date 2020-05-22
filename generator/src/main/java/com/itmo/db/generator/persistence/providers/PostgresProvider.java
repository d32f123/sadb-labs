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
@PropertySource({"classpath:/postgres-provider.properties"})
@EnableJpaRepositories(
        basePackages = "com.itmo.db.generator.persistence.db.postgres",
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
@EntityScan(basePackages = "com.itmo.db.generator.persistence.db.postgres.dao.*")
public class PostgresProvider {

    private final Environment env;

    public PostgresProvider(@Autowired Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource postgresDataSource() {
        return ProviderHelper.getDataSource("postgres.", env);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean postgresEntityManager(
            @Autowired @Qualifier("postgresDataSource") DataSource dataSource
    ) {
        return ProviderHelper.getEntityManager("postgres.", env, dataSource, "com.itmo.db.generator.persistence.db.postgres");
    }

    @Bean
    public PlatformTransactionManager postgresTransactionManager(
            @Autowired @Qualifier("postgresEntityManager") LocalContainerEntityManagerFactoryBean entityManager
    ) {
        return ProviderHelper.getTransactionManager(entityManager);
    }

}
