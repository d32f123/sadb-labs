package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.FieldSource;
import com.itmo.db.generator.persistence.db.mysql.dao.PublicationMySQLDAO;
import com.itmo.db.generator.persistence.db.postgres.repository.SemesterPostgresRepository;
import com.itmo.db.generator.persistence.db.postgres.repository.SpecialtyDisciplineLinkPostgresRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "specialty_disciplines")
@IdClass(SpecialtyDisciplineLinkPostgresDAO.SpecialtyDisciplineLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
@EntityJpaRepository(clazz = SpecialtyDisciplineLinkPostgresRepository.class)
public class SpecialtyDisciplineLinkPostgresDAO implements IdentifiableDAO<SpecialtyDisciplineLinkPostgresDAO.SpecialtyDisciplineLinkPK> {

    @Id
    @Column(name = "specialty_id")
    @FieldSource(source = SpecialtyPostgresDAO.class)
    private Integer specialtyId;
    @Id
    @Column(name = "discipline_id")
    @FieldSource(source = DisciplinePostgresDAO.class)
    private Integer disciplineId;

    public SpecialtyDisciplineLinkPK getId() {
        return new SpecialtyDisciplineLinkPK(
                this.specialtyId,
                this.disciplineId
        );
    }

    public void setId(com.itmo.db.generator.persistence.db.postgres.dao.SpecialtyDisciplineLinkPostgresDAO.SpecialtyDisciplineLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyDisciplineLinkPK implements Serializable {
        private Integer specialtyId;
        private Integer disciplineId;
    }

}
