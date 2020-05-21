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
    @ManyToOne
    @JoinColumn(name = "issue_id", referencedColumnName = "issue_id")
    private IssueMySQLDAO issue;

    @Id
    @ManyToOne
    @JoinColumn(name = "publication_id", referencedColumnName = "publication_id")
    private PublicationMySQLDAO publication;

    public IssuePublicationLinkPK getId() {
        return new IssuePublicationLinkPK(
                this.issue != null ? this.issue.getId() : null,
                this.publication != null ? this.publication.getId() : null
        );
    }

    public void setId(IssuePublicationLinkPK id) {
        throw new UnsupportedOperationException("SetId not implemented");
    }

}
