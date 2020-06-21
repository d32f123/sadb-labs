package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.model.entity.Group;
import com.itmo.db.generator.model.entity.OracleEntity;
import com.itmo.db.generator.model.entity.Person;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.PersonGroupLinkMergeRepository;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoAttribute;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoEntity;
import com.itmo.db.generator.persistence.db.oracle.annotations.ItmoReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@ItmoEntity(description = "PersonGroup link")
@Entity
@EntityJpaRepository(clazz = PersonGroupLinkMergeRepository.class)
public class PersonGroupLink implements AbstractEntity<PersonGroupLink.PersonGroupLinkPK>, OracleEntity {

    @ItmoAttribute
    @EmbeddedId
    private PersonGroupLink.PersonGroupLinkPK id;

    public PersonGroupLink(Integer person_id, Integer group_id) {
        this.id = new PersonGroupLinkPK(person_id, group_id);
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ItmoEntity(description = "PersonGroupLink primary key")
    public static class PersonGroupLinkPK implements OracleEntity, Serializable {
        @ItmoAttribute
        @ItmoReference(Person.class)
        public Integer personId;
        @ItmoAttribute
        @ItmoReference(Group.class)
        public Integer groupId;

        @Override
        public String getName() {
            return this.toString();
        }
    }

    @Override
    public int getMergeKey() {
        return Objects.hash(id.personId, id.groupId);
    }
}
