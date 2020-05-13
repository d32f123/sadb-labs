package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.PersonDAO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<PersonDAO, Long> {
}
