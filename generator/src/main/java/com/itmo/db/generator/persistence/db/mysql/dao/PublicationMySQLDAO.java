package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "publications")
@NoArgsConstructor
@AllArgsConstructor
public class PublicationMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publication_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "citation_index")
    private Integer citation_index;

    @Column(name = "date")
    private Timestamp date;

}
