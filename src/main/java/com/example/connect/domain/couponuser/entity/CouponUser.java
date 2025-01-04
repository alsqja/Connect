package com.example.connect.domain.couponuser.entity;

import com.example.connect.domain.coupon.entity.Coupon;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
import com.example.connect.global.enums.CouponUserStatus;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE coupon_user SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class CouponUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expired_date", nullable = false)
    private LocalDateTime expiredDate;

    @Column(name = "status", nullable = false)
    private CouponUserStatus status;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public CouponUser(LocalDateTime expiredDate, CouponUserStatus status, User user, Coupon coupon) {
        this.expiredDate = expiredDate;
        this.status = status;
        this.user = user;
        this.coupon = coupon;
    }
}
