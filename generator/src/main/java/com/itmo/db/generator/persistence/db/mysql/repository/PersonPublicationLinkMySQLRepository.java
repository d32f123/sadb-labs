package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.PersonPublicationLinkMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonPublicationLinkMySQLRepository
        extends JpaRepository<PersonPublicationLinkMySQLDAO, PersonPublicationLinkMySQLDAO.PersonPublicationLinkPK> {
}
