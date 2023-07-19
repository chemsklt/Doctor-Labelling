package com.cocus.doctorLablling.unitTest;

import com.cocus.doctorLablling.model.Case;
import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.repository.CaseRepository;
import com.cocus.doctorLablling.service.CaseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseServiceImplTest {

    @InjectMocks
    private CaseServiceImpl caseService;

    @Mock
    private CaseRepository caseRepository;

    @Test
    public void testCreateCase() {
        Case caseObj = new Case("5555", "Case Description", 12345L, new ArrayList<>(), LocalDateTime.now());

        when(caseRepository.save(Mockito.any())).thenReturn(caseObj);

        Case createdCase = caseService.createCase(caseObj);

        assertNotNull(createdCase);
        assertEquals("5555", createdCase.getCaseId());
        assertEquals("Case Description", createdCase.getCaseDescription());
        assertEquals(12345L, createdCase.getDoctorId());
        assertNotNull(createdCase.getTimeToLabel());
        assertTrue(createdCase.getLabels().isEmpty());
    }

    @Test
    public void testCreateNewLabelInCase() {
        Case existingCase = new Case("5555", "Case Description", 12345L, new ArrayList<>(), LocalDateTime.now());
        Label label = new Label("2", "Label Description");

        Mockito.when(caseRepository.findById("5555")).thenReturn(Optional.of(existingCase));
        Mockito.when(caseRepository.save(Mockito.any())).thenReturn(existingCase);

        Optional<Case> updatedCase = caseService.createNewLabelInCase("5555", label);

        assertTrue(updatedCase.isPresent());
        assertEquals("5555", updatedCase.get().getCaseId());
        assertEquals("Case Description", updatedCase.get().getCaseDescription());
        assertEquals(12345L, updatedCase.get().getDoctorId());
        assertNotNull(updatedCase.get().getTimeToLabel());
        assertTrue(updatedCase.get().getLabels().contains(label));
    }

    @Test
    public void testCreateNewLabelInCase_NonExistingCase() {
        Label label = new Label("2", "Label Description");

        Mockito.when(caseRepository.findById("5555")).thenReturn(Optional.empty());

        Optional<Case> updatedCase = caseService.createNewLabelInCase("5555", label);

        assertFalse(updatedCase.isPresent());
    }

    @Test
    public void testGetAllCases() {
        List<Case> mockCases = new ArrayList<>();
        mockCases.add(new Case("5555", "Case 1", 12345L, new ArrayList<>(), LocalDateTime.now()));
        mockCases.add(new Case("6666", "Case 2", 67890L, new ArrayList<>(), LocalDateTime.now()));

        Mockito.when(caseRepository.findAll()).thenReturn(mockCases);

        List<Case> cases = caseService.getAllCases();

        assertEquals(2, cases.size());

        Case case1 = cases.get(0);
        assertEquals("5555", case1.getCaseId());
        assertEquals("Case 1", case1.getCaseDescription());
        assertEquals(12345L, case1.getDoctorId());
        assertNotNull(case1.getTimeToLabel());
        assertTrue(case1.getLabels().isEmpty());

        // Add more assertions for the content of the cases list as needed
    }

    @Test
    public void testGetCaseById_ExistingId() {
        Case caseObj = new Case("5555", "Case Description", 12345L, new ArrayList<>(), LocalDateTime.now());
        Optional<Case> optionalCase = Optional.of(caseObj);

        Mockito.when(caseRepository.findById("5555")).thenReturn(optionalCase);

        Optional<Case> result = caseService.getCaseById("5555");

        assertTrue(result.isPresent());
        assertEquals("5555", result.get().getCaseId());
        assertEquals("Case Description", result.get().getCaseDescription());
        assertEquals(12345L, result.get().getDoctorId());
        assertNotNull(result.get().getTimeToLabel());
        assertTrue(result.get().getLabels().isEmpty());
    }

    @Test
    public void testGetCaseById_NonExistingId() {
        Optional<Case> optionalCase = Optional.empty();

        Mockito.when(caseRepository.findById("5555")).thenReturn(optionalCase);

        Optional<Case> result = caseService.getCaseById("5555");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetCasesByLabelCode() {
        List<Case> mockCases = new ArrayList<>();
        mockCases.add(new Case("5555", "Case 1", 12345L, new ArrayList<>(), LocalDateTime.now()));
        mockCases.add(new Case("6666", "Case 2", 67890L, new ArrayList<>(), LocalDateTime.now()));

        Mockito.when(caseRepository.findByLabelCode("labelCode")).thenReturn(mockCases);

        List<Case> cases = caseService.getCasesByLabelCode("labelCode");

        assertEquals(2, cases.size());

        Case case1 = cases.get(0);
        assertEquals("5555", case1.getCaseId());
        assertEquals("Case 1", case1.getCaseDescription());
        assertEquals(12345L, case1.getDoctorId());
        assertNotNull(case1.getTimeToLabel());
        assertTrue(case1.getLabels().isEmpty());

    }

    @Test
    public void testDeleteLabelInCase_ExistingCaseAndLabel() {
        Case caseObj = new Case("5555", "Case Description", 12345L, new ArrayList<>(), LocalDateTime.now());
        Label label = new Label("2", "Label Description");
        caseObj.getLabels().add(label);

        Mockito.when(caseRepository.findById("5555")).thenReturn(Optional.of(caseObj));
        Mockito.when(caseRepository.save(Mockito.any())).thenReturn(caseObj);

        boolean deleted = caseService.deleteLabelInCase("5555", "2");

        assertTrue(deleted);
        assertFalse(caseObj.getLabels().contains(label));
    }

    @Test
    public void testDeleteLabelInCase_NonExistingCase() {
        Mockito.when(caseRepository.findById("5555")).thenReturn(Optional.empty());

        boolean deleted = caseService.deleteLabelInCase("5555", "2");

        assertFalse(deleted);
    }
}
