package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.PersonProjectLinkMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonProjectLinkMySQLRepository
        extends JpaRepository<PersonProjectLinkMySQLDAO, PersonProjectLinkMySQLDAO.PersonProjectLinkPK> {
}
