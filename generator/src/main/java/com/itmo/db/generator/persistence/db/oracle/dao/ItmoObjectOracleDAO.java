package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "itmo_objects")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItmoObjectOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "object_type_id", referencedColumnName = "id")
    private ItmoObjectTypeOracleDAO itmoObjectTypeOracleDAO;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ItmoObjectOracleDAO parent;
}
