package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

@MergeRepository
public interface AccommodationRecordMergeRepository extends JpaRepository<AccommodationRecord, Long> {
}
