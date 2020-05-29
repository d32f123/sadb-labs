package com.itmo.db.generator.persistence.db.mongo.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("room_records")
public class AccommodationRecordMongoDAO implements IdentifiableDAO<String> {

    @Id
    private String id;

    @Field(name = "facilities")
    private Boolean facilities;

    @Field(name = "budget")
    private Boolean budget;

    @Field(name = "payment")
    private Double payment;

    @Field(name = "living_start_date")
    private Date livingStartDate;

    @Field(name = "living_end_date")
    private Date livingEndDate;

    @Field(name = "course")
    private String course;

    @Field(name = "person_id")
    private String personId;

    @Field(name = "room_id")
    private String roomId;
}

