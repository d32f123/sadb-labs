package com.itmo.db.generator.persistence.db.oracle.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "itmo_list_values")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItmoListValueOracleDAO implements IdentifiableDAO<Long> {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;
}
