package com.itmo.db.generator.model.entity.link;

import com.itmo.db.generator.model.entity.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IssuePublicationLink implements AbstractEntity<IssuePublicationLink.IssuePublicationLinkPK> {

    public IssuePublicationLink(Integer issue_id, Integer publication_id){
        this.id = new IssuePublicationLinkPK(issue_id, publication_id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssuePublicationLinkPK {
        public Integer issueId;
        public Integer publicationId;
    }

    private IssuePublicationLinkPK id;
}
