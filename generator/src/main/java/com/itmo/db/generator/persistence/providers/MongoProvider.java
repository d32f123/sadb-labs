package com.itmo.db.generator.persistence.providers;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Objects;

@Configuration
@PropertySource({"classpath:/mongo-provider.properties"})
@EnableMongoRepositories(
        basePackages = "com.itmo.db.generator.persistence.db.mongo.repository"
)
@EntityScan(
        basePackages = "com.itmo.db.generator.persistence.db.mongo.dao.*"
)
public class MongoProvider extends AbstractMongoClientConfiguration {

    private final Environment env;

    public MongoProvider(@Autowired Environment env) {
        this.env = env;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        // TODO: Add auth here via MongoClientSettings
        return MongoClients.create(Objects.requireNonNull(env.getProperty("mongo.spring.datasource.url")));
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("mongo.db.name");
    }
}
