package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.PersonMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonMySQLRepository extends JpaRepository<PersonMySQLDAO, Long> {
}
