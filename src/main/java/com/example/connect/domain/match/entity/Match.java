package com.example.connect.domain.match.entity;

import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
import com.example.connect.global.enums.MatchStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "match")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE match SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "deleted_at", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User toUser;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public Match(MatchStatus status, Schedule schedule, User fromUser, User toUser) {
        this.status = status;
        this.schedule = schedule;
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
