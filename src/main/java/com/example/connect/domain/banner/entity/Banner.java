package com.example.connect.domain.banner.entity;

import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "banner")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE banner SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expiredDate;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public Banner(String profileUrl, String content, LocalDateTime expiredDate) {
        this.profileUrl = profileUrl;
        this.content = content;
        this.expiredDate = expiredDate;
    }
}
