package com.itmo.db.generator.persistence.db.mongo.dao;

import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.model.entity.Dormitory;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.DormitoryMongoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("dormitories")
@EntityJpaRepository(clazz = DormitoryMongoRepository.class)
public class DormitoryMongoDAO implements IdentifiableDAO<String> {

    @Id
    private String id;

    @Field(name = "address")
    private String address;

    @Field(name = "room_count")
    private Integer roomCount;

}
