package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.PersonProjectLinkDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonProjectLinkRepository
        extends JpaRepository<PersonProjectLinkDAO, PersonProjectLinkDAO.PersonProjectLinkPK> {
}
