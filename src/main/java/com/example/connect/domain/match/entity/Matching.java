package com.example.connect.domain.match.entity;

import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.schedule.entity.Schedule;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matching")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE matching SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "similarity", nullable = false)
    private double similarity;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "schedule_from_id")
    private Schedule fromSchedule;

    @ManyToOne
    @JoinColumn(name = "schedule_to_id")
    private Schedule toSchedule;

    @OneToMany(mappedBy = "matching", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public Matching(MatchStatus status, Schedule fromSchedule, Schedule toSchedule, double similarity) {
        this.status = status;
        this.fromSchedule = fromSchedule;
        this.toSchedule = toSchedule;
        this.similarity = similarity;
    }
}
