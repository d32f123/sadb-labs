package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.merge.annotations.FetchRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

@FetchRepository
public interface PublicationMySQLRepository extends JpaRepository<PublicationMySQLDAO, Long> {
}
