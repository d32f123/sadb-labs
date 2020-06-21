package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.UniversityMergeRepository;
import com.itmo.db.generator.persistence.db.postgres.dao.StudentPostgresDAO;
import com.itmo.db.generator.persistence.db.postgres.dao.UniversityPostgresDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = UniversityMergeRepository.class)
@DAO(clazzes = UniversityPostgresDAO.class)
public class University implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private LocalDate creationDate;

    @Override
    public int getMergeKey() {
        return Objects.hash(name);
    }
}
