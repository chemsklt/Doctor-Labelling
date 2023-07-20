package com.cocus.doctorLablling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medical_case")
public class Case {
    @Id
    @Column(name = "case_id", nullable = false, unique = true)
    private String caseId;

    @Column(name = "case_description", nullable = false)
    private String caseDescription;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Label> labels = new ArrayList<>();

    @Column(name = "time_to_label", nullable = false)
    private LocalDateTime timeToLabel;
}
