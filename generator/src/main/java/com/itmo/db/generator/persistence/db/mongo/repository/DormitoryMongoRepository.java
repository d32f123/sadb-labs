package com.itmo.db.generator.persistence.db.mongo.repository;

import com.itmo.db.generator.persistence.db.mongo.dao.DormitoryMongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DormitoryMongoRepository extends MongoRepository<DormitoryMongoDAO, String> {
}
