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
@PropertySource({"classpath:/merge-provider.properties"})
@EnableJpaRepositories(
        basePackages = "com.itmo.db.generator.persistence.db.merge",
        entityManagerFactoryRef = "mergeEntityManager",
        transactionManagerRef = "mergeTransactionManager"
)
@EntityScan(basePackages = {"com.itmo.db.generator.model.entity.*", "com.itmo.db.generator.model.entity.link.*"})
public class MergeProvider {
    @Autowired
    private final Environment env;

    public MergeProvider(@Autowired Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource mergeDataSource() {
        var mergeDataSource = ProviderHelper.getDataSource("merge.", env);
        mergeDataSource.setSchema(env.getProperty("merge.spring.datasource.schema"));
        return mergeDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mergeEntityManager(
            @Autowired @Qualifier("mergeDataSource") DataSource dataSource
    ) {
        return ProviderHelper.getEntityManager("merge.", env, dataSource, "com.itmo.db.generator.model.entity");
    }

    @Bean
    public PlatformTransactionManager mergeTransactionManager(
            @Autowired @Qualifier("mergeEntityManager") LocalContainerEntityManagerFactoryBean entityManager
    ) {
        return ProviderHelper.getTransactionManager(entityManager);
    }
}
