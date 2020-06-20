package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

@MergeRepository
public interface DormitoryMergeRepository extends MongoRepository<Dormitory, Long> {
}
