package com.itmo.db.generator.persistence.db.mongo.repository;

import com.itmo.db.generator.persistence.db.mongo.dao.PersonMongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonMongoRepository extends MongoRepository<PersonMongoDAO, String> {
}

