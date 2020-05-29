package com.itmo.db.generator.persistence.db.mongo.repository;

import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomMongoRepository extends MongoRepository<RoomMongoDAO, String> {
}

