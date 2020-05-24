package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "library_records")
@NoArgsConstructor
@AllArgsConstructor
public class LibraryRecordMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_record_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private PersonMySQLDAO person;

    @Column(name = "book_id")
    private String bookId;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private Date actionDate;

}
