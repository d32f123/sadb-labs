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

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "attribute_id")
    private Long attributeId;

    @Column(name = "value")
    private String value;

    @Column(name = "list_value_id")
    private Long listValueId;

}
