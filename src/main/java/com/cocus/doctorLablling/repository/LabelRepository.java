package com.cocus.doctorLablling.repository;

import com.cocus.doctorLablling.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {
    // Add any custom query methods for Label entity if needed.
}