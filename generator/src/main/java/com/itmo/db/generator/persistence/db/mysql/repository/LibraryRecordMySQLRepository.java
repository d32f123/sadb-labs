package com.itmo.db.generator.persistence.db.mysql.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.LibraryRecordMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRecordMySQLRepository extends JpaRepository<LibraryRecordMySQLDAO, Long> {
}
