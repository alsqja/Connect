package com.example.connect.domain.point.entity;

import com.example.connect.domain.payment.entity.Payment;
import com.example.connect.domain.pointuse.entity.PointUse;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "is_zero", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isZero;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointUse> pointUses = new ArrayList<>();

    public Point(BigDecimal amount, User user, Payment payment, Boolean isZero) {
        this.amount = amount;
        this.user = user;
        this.payment = payment;
        this.isZero = isZero;
    }

    public void pointUpdate(Boolean isZero) {
        this.isZero = isZero;
    }
}
