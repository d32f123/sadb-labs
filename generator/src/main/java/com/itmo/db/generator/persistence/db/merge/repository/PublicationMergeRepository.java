package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.Publication;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

@MergeRepository
public interface PublicationMergeRepository extends JpaRepository<Publication, Long> {
}
