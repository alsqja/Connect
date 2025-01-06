package com.example.connect.domain.address.entity;

import com.example.connect.domain.user.entity.User;
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

@Entity
@Table(name = "address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public Address(User user, String address, Double latitude, Double longitude, Boolean isActive) {
        this.user = user;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = isActive;
    }

    public Address(String address, Double latitude, Double longitude, Boolean isActive) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = isActive;
    }

    public void updateUser(User user) {
        this.user = user;
    }
}
