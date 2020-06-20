package com.itmo.db.generator.persistence.db.mongo.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.AccommodationRecordMongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;
@FetchRepository
public interface AccommodationRecordMongoRepository extends MongoRepository<AccommodationRecordMongoDAO, String> {
}
