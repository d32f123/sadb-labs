package com.itmo.db.generator.persistence.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:/mysql-provider.properties"})
@EnableJpaRepositories(
        basePackages = "com.itmo.db.generator.persistence.db.mysql",
        entityManagerFactoryRef = "mysqlEntityManager",
        transactionManagerRef = "mysqlTransactionManager"
)
@EntityScan(basePackages = "com.itmo.db.generator.persistence.db.mysql.dao.*")
public class MySQLProvider {

    @Autowired
    private final Environment env;

    public MySQLProvider(@Autowired Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource mysqlDataSource() {
        return ProviderHelper.getDataSource("mysql.", env);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mysqlEntityManager(
            @Autowired @Qualifier("mysqlDataSource") DataSource dataSource
    ) {
        return ProviderHelper.getEntityManager("mysql.", env, dataSource, "com.itmo.db.generator.persistence.db.mysql");
    }

    @Bean
    public PlatformTransactionManager mysqlTransactionManager(
            @Autowired @Qualifier("mysqlEntityManager") LocalContainerEntityManagerFactoryBean entityManager
    ) {
        return ProviderHelper.getTransactionManager(entityManager);
    }

}
