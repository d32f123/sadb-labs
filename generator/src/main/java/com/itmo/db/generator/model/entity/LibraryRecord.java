package com.itmo.db.generator.model.entity;

import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.LibraryRecordMergeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityJpaRepository(clazz = LibraryRecordMergeRepository.class)
public class LibraryRecord implements NumericallyIdentifiableEntity {

    @Id
    private Integer id;
    private Integer personId;
    private String bookId;
    private String action;
    private LocalDate actionDate;

}
