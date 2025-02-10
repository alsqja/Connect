package com.example.connect.domain.payment.entity;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
import com.example.connect.global.enums.PaymentStatus;
import com.example.connect.global.enums.PaymentType;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE payment SET is_deleted = true WHERE id = ?")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_uid", nullable = false, unique = true)
    private String payUid;

    @Column(name = "portone_uid", nullable = false, unique = true)
    private String portoneUid;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "pay_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus payStatus;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> mamberships = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

    public Payment(String payUid, String portoneUid, BigDecimal amount, PaymentType type, PaymentStatus payStatus, String details, User user) {
        this.payUid = payUid;
        this.portoneUid = portoneUid;
        this.amount = amount;
        this.type = type;
        this.payStatus = payStatus;
        this.details = details;
        this.user = user;
    }

    public void updatePayStatus(PaymentStatus payStatus, String details) {
        this.payStatus = payStatus;
        this.details = details;
    }
}
