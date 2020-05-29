package com.itmo.db.generator.persistence.impl;

import com.itmo.db.generator.generator.Generator;
import com.itmo.db.generator.model.entity.AccommodationRecord;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.model.entity.Room;
import com.itmo.db.generator.persistence.AbstractPersistenceWorker;
import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.AccommodationRecordMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.PersonMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.dao.RoomMongoDAO;
import com.itmo.db.generator.persistence.db.mongo.repository.AccommodationRecordMongoRepository;

import java.util.List;

public class AccommodationRecordPersistenceWorker extends AbstractPersistenceWorker<AccommodationRecord, Integer> {

    private final AccommodationRecordMongoRepository accommodationRecordMongoRepository;

    public AccommodationRecordPersistenceWorker(Generator generator, AccommodationRecordMongoRepository accommodationRecordMongoRepository) {
        super(AccommodationRecord.class, generator);
        this.accommodationRecordMongoRepository = accommodationRecordMongoRepository;
    }

    @Override
    protected List<? extends IdentifiableDAO<?>> doPersist(AccommodationRecord entity) {
        AccommodationRecordMongoDAO dao = new AccommodationRecordMongoDAO(
                null,
                entity.isFacilities(),
                entity.isBudget(),
                entity.getPayment(),
                entity.getLivingStartDate(),
                entity.getLivingEndDate(),
                entity.getCourse(),
                this.getDependencyDAOId(Person.class, entity.getPersonId(), PersonMongoDAO.class),
                this.getDependencyDAOId(Room.class, entity.getRoomId(), RoomMongoDAO.class)
        );

        this.accommodationRecordMongoRepository.save(dao);
        return List.of(dao);
    }

    @Override
    protected void doCommit() {
    }
}
