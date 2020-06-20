package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.mysql.repository.LibraryRecordMySQLRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "library_records")
@NoArgsConstructor
@AllArgsConstructor
@EntityJpaRepository(clazz = LibraryRecordMySQLRepository.class)
public class LibraryRecordMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_record_id")
    private Long id;

    @Column(name = "person_id")
    private Long personId;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Timestamp actionDate;

}
