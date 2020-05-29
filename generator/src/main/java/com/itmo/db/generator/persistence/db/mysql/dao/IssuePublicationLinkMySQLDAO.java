package com.itmo.db.generator.persistence.db.mysql.dao;

import com.itmo.db.generator.persistence.db.IdentifiableDAO;
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
public class IssuePublicationLinkMySQLDAO implements IdentifiableDAO<IssuePublicationLinkMySQLDAO.IssuePublicationLinkPK> {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssuePublicationLinkPK implements Serializable {
        private Long issue;
        private Long publication;
    }

    @Id
    @Column(name = "issue_id")
    private Long issueId;

    @Id
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
