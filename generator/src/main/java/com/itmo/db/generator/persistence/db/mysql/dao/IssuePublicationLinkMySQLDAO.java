package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
import com.itmo.db.generator.persistence.db.merge.annotations.EntityJpaRepository;
import com.itmo.db.generator.persistence.db.merge.annotations.FieldSource;
import com.itmo.db.generator.persistence.db.mysql.repository.IssuePublicationLinkMySQLRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "issue_publications")
@IdClass(IssuePublicationLinkMySQLDAO.IssuePublicationLinkPK.class)
@AllArgsConstructor
@NoArgsConstructor
@EntityJpaRepository(clazz = IssuePublicationLinkMySQLRepository.class)
public class IssuePublicationLinkMySQLDAO implements IdentifiableDAO<IssuePublicationLinkMySQLDAO.IssuePublicationLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssuePublicationLinkPK implements Serializable {
        private Long issueId;
        private Long publicationId;
    }

    @Id
    @FieldSource(source = IssueMySQLDAO.class)
    @Column(name = "issue_id")
    private Long issueId;

    @Id
    @FieldSource(source = PublicationMySQLDAO.class)
    @Column(name = "publication_id")
    private Long publicationId;

    public IssuePublicationLinkPK getId() {
        return new IssuePublicationLinkPK(
                this.issueId,
                this.publicationId
        );
    }

    public void setId(IssuePublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
