package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.LibraryRecordMergeRepository;
import com.itmo.db.generator.persistence.db.mongo.dao.DormitoryMongoDAO;
import com.itmo.db.generator.persistence.db.mysql.dao.LibraryRecordMySQLDAO;
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
@EntityJpaRepository(clazz = LibraryRecordMergeRepository.class)
@DAO(clazzes = LibraryRecordMySQLDAO.class)
public class LibraryRecord implements NumericallyIdentifiableEntity {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer personId;
    private String bookId;
    private String action;
    private LocalDate actionDate;

    @Override
    public int getMergeKey() {
        return Objects.hash(personId, bookId, action, actionDate);
    }
}
