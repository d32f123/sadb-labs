package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "issues")
@NoArgsConstructor
@AllArgsConstructor
public class IssueMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "location")
    private String location;

    @Column(name = "length", nullable = false)
    private Integer length;

    @Column(name = "format", nullable = false)
    private String format;
}
