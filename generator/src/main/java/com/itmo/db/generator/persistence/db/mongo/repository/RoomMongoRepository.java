package com.itmo.db.generator.persistence.db.mongo.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

@FetchRepository
public interface RoomMongoRepository extends MongoRepository<RoomMongoDAO, String> {
}

