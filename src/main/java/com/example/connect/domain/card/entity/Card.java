package com.example.connect.domain.card.entity;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "card")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE card SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "expired_year", nullable = false)
    private String expiredYear;

    @Column(name = "expired_month", nullable = false)
    private String expiredMonth;

    @Column(name = "card_password", nullable = false)
    private String cardPassword;

    @Column(name = "birth", nullable = false)
    private String birth;

    @ManyToOne
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public Card(String cardNumber, String expiredYear, String expiredMonth, String cardPassword, String birth, Membership membership) {
        this.cardNumber = cardNumber;
        this.expiredYear = expiredYear;
        this.expiredMonth = expiredMonth;
        this.cardPassword = cardPassword;
        this.birth = birth;
        this.membership = membership;
    }
}
