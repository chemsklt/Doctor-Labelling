package com.cocus.doctorLablling.service;

import com.cocus.doctorLablling.model.Case;
import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.repository.CaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CaseServiceImpl implements CaseService{

    @Autowired
    private CaseRepository caseRepository;

    @Override
    @Cacheable(cacheNames = "cases")
    public Case createCase(Case caseObj) {
        log.info("Creating a new Case: {}", caseObj);
        caseObj.setTimeToLabel(LocalDateTime.now());
        Case createdCase =caseRepository.save(caseObj);
        log.info("Created Case: {}", createdCase);
        return createdCase;
    }

    @Override
    @Cacheable(cacheNames = "cases")
    public Optional<Case> createNewLabelInCase(String caseId, Label label) {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            Case caseObj = optionalCase.get();
            caseObj.getLabels().add(label);
            caseObj.setTimeToLabel(LocalDateTime.now());
            Case updatedCase = caseRepository.save(caseObj);
            return Optional.of(updatedCase);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Cacheable(cacheNames = "cases")
    public List<Case> getAllCases() {
        log.info("Retrieving all Cases");
        return caseRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "cases")
    public Optional<Case> getCaseById(String caseId) {
        log.info("Retrieving Case with ID: {}", caseId);
        return caseRepository.findById(caseId);
    }

    @Override
    @Cacheable(cacheNames = "cases")
    public List<Case> getCasesByLabelCode(String labelCode) {
        log.info("Retrieving Case with label Code: {}", labelCode);
        return caseRepository.findByLabelCode(labelCode);
    }

    @Override
    @Cacheable(cacheNames = "cases")
    public boolean deleteLabelInCase(String caseId, String labelCode) {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            Case caseObj = optionalCase.get();

            List<Label> labels = caseObj.getLabels();
            labels.removeIf(label -> label.getCode().equals(labelCode));

            caseRepository.save(caseObj);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Cacheable(cacheNames = "cases")
    public Optional<Case> updateCase(String caseId, Case caseObj) {
        log.info("Updating Casewith ID: {}", caseId);
        Optional<Case> existingCase = caseRepository.findById(caseId);
        if (existingCase.isPresent()) {
            Case aCase = existingCase.get();
            aCase.setCaseDescription(caseObj.getCaseDescription());
            aCase.setDoctorId(caseObj.getDoctorId());
            Case updatedCase = caseRepository.save(aCase);
            log.info("Updated Case: {}", updatedCase);
            return Optional.of(updatedCase);
        } else {
            return Optional.empty();
        }
    }
}

