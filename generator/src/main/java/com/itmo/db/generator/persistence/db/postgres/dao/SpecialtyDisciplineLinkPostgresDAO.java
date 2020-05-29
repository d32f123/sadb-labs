package com.itmo.db.generator.persistence.db.postgres.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
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
public class SpecialtyDisciplineLinkPostgresDAO implements IdentifiableDAO<SpecialtyDisciplineLinkPostgresDAO.SpecialtyDisciplineLinkPK> {

    @Id
    @Column(name = "specialty_id")
    private Integer specialtyId;
    @Id
    @Column(name = "discipline_id")
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
        private Integer specialty;
        private Integer discipline;
    }

}
