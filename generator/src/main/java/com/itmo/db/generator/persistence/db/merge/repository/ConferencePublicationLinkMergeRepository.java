package com.itmo.db.generator.persistence.db.merge.repository;

import com.itmo.db.generator.persistence.db.mysql.dao.ConferencePublicationLinkMySQLDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferencePublicationLinkMergeRepository
        extends JpaRepository<ConferencePublication, ConferencePublicationLinkMySQLDAO.ConferencePublicationLinkPK> {
}
