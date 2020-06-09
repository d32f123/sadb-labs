package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "persons")
@NoArgsConstructor
@AllArgsConstructor
public class PersonMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "patronymic_name")
    private String patronymicName;

    @Column(name = "role")
    private String role;

    @Column(name = "person_number")
    private String personNumber;

}
