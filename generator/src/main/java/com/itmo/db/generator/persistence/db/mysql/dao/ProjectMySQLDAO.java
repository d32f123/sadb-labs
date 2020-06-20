package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.mysql.repository.PersonPublicationLinkMySQLRepository;
import com.itmo.db.generator.persistence.db.mysql.repository.ProjectMySQLRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@EntityJpaRepository(clazz = ProjectMySQLRepository.class)
public class ProjectMySQLDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "name")
    private String name;

}
