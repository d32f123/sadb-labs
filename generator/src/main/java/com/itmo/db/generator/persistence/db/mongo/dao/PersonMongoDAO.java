package com.itmo.db.generator.persistence.db.mongo.dao;

import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.PersonMongoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("persons")
@EntityJpaRepository(clazz = PersonMongoRepository.class)
public class PersonMongoDAO implements IdentifiableDAO<String> {
    @Id
    private String id;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "patronymic_name")
    private String patronymicName;

    @Field(name = "is_in_dormitory")
    private boolean isInDormitory;

    @Field(name = "warning_count")
    private Integer warningCount;

    @Field(name = "person_number")
    private String personNumber;
}
