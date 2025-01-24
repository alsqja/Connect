package com.example.connect.domain.userimage.entity;

import com.example.connect.domain.user.entity.User;
import com.example.connect.global.common.BaseEntity;
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
@Table(name = "user_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserImage(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateFields(String url, String description) {
        if (url != null) {
            this.url = url;
        }
        if (description != null) {
            this.description = description;
        }
    }
}
