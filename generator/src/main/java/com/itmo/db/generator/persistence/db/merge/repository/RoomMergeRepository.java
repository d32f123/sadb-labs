package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import org.springframework.data.jpa.repository.JpaRepository;

@MergeRepository
public interface RoomMergeRepository extends JpaRepository<RoomMongoDAO, Long> {
}

