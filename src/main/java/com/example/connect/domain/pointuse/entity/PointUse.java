package com.example.connect.domain.pointuse.entity;

import com.example.connect.domain.point.entity.Point;
import com.example.connect.global.common.BaseEntity;
import com.example.connect.global.enums.PointUseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "point_use")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointUse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "point_change", nullable = false)
    private BigDecimal pointChange;

    @Column(name = "details", nullable = false)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PointUseType pointUseType;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;

    public PointUse(BigDecimal amount, BigDecimal pointChange, String details, PointUseType pointUseType, Point point) {
        this.amount = amount;
        this.pointChange = pointChange;
        this.details = details;
        this.pointUseType = pointUseType;
        this.point = point;
    }
}
