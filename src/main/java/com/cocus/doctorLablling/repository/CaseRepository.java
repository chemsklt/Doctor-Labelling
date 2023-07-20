package com.cocus.doctorLablling.repository;

import com.cocus.doctorLablling.model.Case;
import com.cocus.doctorLablling.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, String> {

    @Query("SELECT c FROM Case c JOIN c.labels l WHERE l.code = :labelCode AND SIZE(c.labels) > 0")
    List<Case> findByLabelCode(@Param("labelCode") String labelCode);
}

