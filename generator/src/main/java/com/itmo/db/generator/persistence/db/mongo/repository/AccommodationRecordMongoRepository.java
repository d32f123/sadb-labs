package com.itmo.db.generator.persistence.db.mongo.repository;

import com.itmo.db.generator.persistence.db.mongo.dao.AccommodationRecordMongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccommodationRecordMongoRepository extends MongoRepository<AccommodationRecordMongoDAO, String> {
}
