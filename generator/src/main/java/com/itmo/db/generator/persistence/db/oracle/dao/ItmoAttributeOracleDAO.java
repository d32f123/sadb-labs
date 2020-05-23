package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@Table(name = "itmo_attributes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItmoAttributeOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "attribute_type", nullable = false)
    private String attributeType;

}
