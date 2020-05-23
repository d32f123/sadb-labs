package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "itmo_params")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItmoParamOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "object_id", referencedColumnName = "id")
    private ItmoObjectOracleDAO itmoObjectOracleDAO;

    @ManyToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private ItmoAttributeOracleDAO itmoAttributeOracleDAO;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "list_value_id", referencedColumnName = "id")
    private ItmoListValueOracleDAO itmoListValueOracleDAO;

}
