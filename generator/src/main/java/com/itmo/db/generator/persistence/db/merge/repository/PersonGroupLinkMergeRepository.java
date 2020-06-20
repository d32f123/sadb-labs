package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.model.entity.link.PersonGroupLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonGroupLinkMergeRepository extends JpaRepository<
        PersonGroupLink, PersonGroupLink.PersonGroupLinkPK> {
}
