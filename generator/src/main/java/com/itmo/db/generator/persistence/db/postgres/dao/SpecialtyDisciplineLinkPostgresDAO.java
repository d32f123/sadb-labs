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
    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "specialty_id")
    private SpecialtyPostgresDAO specialty;
    @Id
    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "discipline_id")
    private DisciplinePostgresDAO discipline;

    public SpecialtyDisciplineLinkPK getId() {
        return new SpecialtyDisciplineLinkPK(
                this.specialty != null ? this.specialty.getId() : null,
                this.discipline != null ? this.discipline.getId() : null
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
