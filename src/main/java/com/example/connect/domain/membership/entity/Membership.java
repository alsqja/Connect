package com.example.connect.domain.membership.entity;

import com.example.connect.global.common.BaseEntity;
import com.example.connect.global.enums.MembershipType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "membership")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE membership SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipType type;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "deleted_at", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    public Membership(MembershipType type, LocalDateTime expiredAt) {
        this.type = type;
        this.expiredAt = expiredAt;
    }
}
