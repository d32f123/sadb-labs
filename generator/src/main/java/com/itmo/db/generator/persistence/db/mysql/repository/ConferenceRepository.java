package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.ConferenceDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<ConferenceDAO, Long> {
}
