package com.example.connect.domain.user.entity;

import com.example.connect.domain.couponuser.entity.CouponUser;
import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.point.entity.Point;
import com.example.connect.domain.report.entity.Report;
import com.example.connect.domain.schedule.entity.Schedule;
import com.example.connect.global.common.BaseEntity;
import com.example.connect.global.enums.Gender;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private String birth;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "profile_url", nullable = true)
    private String profileUrl;

    @Column(name = "is_active_matching", nullable = false)
    private Boolean isActiveMatching;

    @Column(name = "reported_count", nullable = false)
    private Integer reportedCount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> sentReports = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> points = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponUser> couponUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membership> memberships = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = UserStatus.NORMAL;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
        if (reportedCount == null) {
            reportedCount = 0;
        }
    }

    public User(String email, String password, String name, String birth, Gender gender, String profileUrl, Boolean isActiveMatching, UserRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.profileUrl = profileUrl;
        this.isActiveMatching = isActiveMatching;
        this.role = role;
    }

    public User(UserAdminOnly findUser) {
        this.id = findUser.getId();
        this.email = findUser.getEmail();
        this.name = findUser.getName();
        this.birth = findUser.getBirth();
        this.gender = findUser.getGender();
        this.profileUrl = findUser.getProfileUrl();
        this.status = findUser.getStatus();
        this.role = findUser.getRole();
        this.isDeleted = findUser.getIsDeleted();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserFields(String name, String profileUrl, Boolean isActiveMatching) {
        if (name != null) {
            this.name = name;
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
        if (isActiveMatching != null) {
            this.isActiveMatching = isActiveMatching;
        }
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    public void addReportedCount() {
        this.reportedCount++;
    }

    public void minusReportedCount() {
        this.reportedCount--;
    }
}
