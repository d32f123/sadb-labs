package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.ProfessorMergeRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.LibraryRecordMySQLDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.ProfessorPostgresDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = ProfessorMergeRepository.class)
@DAO(clazzes = ProfessorPostgresDAO.class)
public class Professor implements AbstractEntity<Integer> {
    @Id
    @GeneratedValue
    private Integer id; // personId
    private Integer facultyId;
    private String personNumber;

    @Override
    public int getMergeKey() {
        return Objects.hash(personNumber);
    }
}
