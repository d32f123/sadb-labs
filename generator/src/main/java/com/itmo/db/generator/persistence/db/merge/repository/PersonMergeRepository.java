package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

@MergeRepository
public interface PersonMergeRepository extends JpaRepository<Person, Long> {
}
