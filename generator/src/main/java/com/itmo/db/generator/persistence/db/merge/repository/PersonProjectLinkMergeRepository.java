package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.MergeRepository;

@MergeRepository
public interface PersonProjectLinkMergeRepository
        extends JpzaRepository<PersonProjectLinkMergedDAO, PersonProjectLinkMergedDAO.PersonProjectLinkPK> {
}
