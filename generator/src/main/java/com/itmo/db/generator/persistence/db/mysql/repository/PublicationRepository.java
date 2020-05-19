package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.PublicationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<PublicationDAO, Long> {
}
