package com.itmo.db.generator.persistence.db.oracle.repository;

import com.itmo.db.generator.persistence.db.oracle.dao.PersonGroupLinkOracleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonGroupLinkOracleRepository extends JpaRepository<PersonGroupLinkOracleDAO, PersonGroupLinkOracleDAO.PersonGroupLinkPK> {
}
