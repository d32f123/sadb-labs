package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.link.SpecialtyDisciplineLink;
import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

@MergeRepository
public interface SpecialtyDisciplineLinkMergeRepository
        extends JpaRepository<SpecialtyDisciplineLink, SpecialtyDisciplineLink.SpecialtyDisciplineLinkPK> {
}
