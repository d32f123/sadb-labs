package com.itmo.db.generator.persistence.db.mongo.dao;

import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.FieldSource;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;
import com.itmo.db.generator.persistence.db.mongo.repository.RoomMongoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("rooms")
@EntityJpaRepository(clazz = RoomMongoRepository.class)
public class RoomMongoDAO implements IdentifiableDAO<String> {

    @Id
    private String id;

    @Field(name = "room_number")
    private Integer roomNumber;


    @Field(name = "capacity")
    private Integer capacity;

    @Field(name = "engaged")
    private Integer engaged;

    @Field(name = "bugs")
    private Boolean bugs;

    @Field(name = "last_cleaning_date")
    private Date lastCleaningDate;

    @Field(name = "dormitory_id")
    @FieldSource(source = DormitoryMongoDAO.class)
    private String dormitoryId;

}
