package com.example.connect.domain.membership.repository;

import com.example.connect.domain.membership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface membershipRepository extends JpaRepository<Membership, Long> {
    default Membership findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
