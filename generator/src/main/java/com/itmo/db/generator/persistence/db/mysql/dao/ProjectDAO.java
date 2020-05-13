package com.itmo.db.generator.persistence.db.mysql.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

}
