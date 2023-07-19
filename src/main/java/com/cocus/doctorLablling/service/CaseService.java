package com.cocus.doctorLablling.service;

import com.cocus.doctorLablling.model.Case;
import com.cocus.doctorLablling.model.Label;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CaseService {
    Case createCase(Case caseObj);

    Optional<Case> createNewLabelInCase(String caseId, Label label);

    List<Case> getAllCases();

    Optional<Case> getCaseById(String caseId);

    List<Case> getCasesByLabelCode(String labelCode);

    boolean deleteLabelInCase(String caseId, String labelCode);

    Optional<Case> updateCase(String caseId, Case caseObj);
}