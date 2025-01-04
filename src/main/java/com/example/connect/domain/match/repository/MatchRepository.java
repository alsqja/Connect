package com.example.connect.domain.match.repository;

import com.example.connect.domain.match.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Matching, Long> {
    default Matching findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
