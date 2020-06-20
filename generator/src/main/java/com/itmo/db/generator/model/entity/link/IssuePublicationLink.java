package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import com.itmo.db.generator.persistence.db.merge.annotations.DAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.repository.IssuePublicationLinkMergeRepository;
import com.itmo.db.generator.persistence.db.mysql.dao.IssuePublicationLinkMySQLDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@DAO(clazzes = IssuePublicationLinkMySQLDAO.class)
@EntityJpaRepository(clazz = IssuePublicationLinkMergeRepository.class)
public class IssuePublicationLink implements AbstractEntity<IssuePublicationLink.IssuePublicationLinkPK> {

    public IssuePublicationLink(Integer issue_id, Integer publication_id) {
        this.id = new IssuePublicationLinkPK(issue_id, publication_id);
    }

    @EmbeddedId
    private IssuePublicationLinkPK id;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class IssuePublicationLinkPK implements Serializable {
        public Integer issueId;
        public Integer publicationId;
    }
}
