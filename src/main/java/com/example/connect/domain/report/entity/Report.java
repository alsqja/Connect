package com.example.connect.domain.report.entity;

import com.example.connect.domain.match.entity.Match;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE report SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User toUser;

    public Report(String content, Match match, User fromUser, User toUser) {
        this.content = content;
        this.match = match;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
