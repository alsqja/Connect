package com.example.connect.domain.coupon.entity;

import com.example.connect.domain.couponuser.entity.CouponUser;
import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE coupon SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "expired_date", nullable = false)
    private LocalDate expiredDate;

    @Column(name = "open_date", nullable = false)
    private LocalDateTime openDate;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponUser> couponUsers = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public Coupon(String name, String description, Integer count, Integer amount, LocalDate expiredDate, LocalDateTime openDate) {
        this.name = name;
        this.description = description;
        this.count = count;
        this.amount = amount;
        this.expiredDate = expiredDate;
        this.openDate = openDate;
    }

    public void updateCoupon(String name, String description, Integer count, Integer amount, LocalDate expiredDate, LocalDateTime openDate, Boolean isDeleted) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (count != null) {
            this.count = count;
        }
        if (amount != null) {
            this.amount = amount;
        }
        if (expiredDate != null) {
            this.expiredDate = expiredDate;
        }
        if (openDate != null) {
            this.openDate = openDate;
        }
        if (isDeleted != null) {
            this.isDeleted = isDeleted;
        }
    }
}
