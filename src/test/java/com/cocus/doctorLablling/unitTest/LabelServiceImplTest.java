package com.cocus.doctorLablling.unitTest;

import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.repository.LabelRepository;
import com.cocus.doctorLablling.service.LabelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LabelServiceImplTest {

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    private Label mockLabel;

    @BeforeEach
    public void setUp() {
        mockLabel = new Label("1", "Mock Label");
    }

    @Test
    public void testCreateLabel() {
        when(labelRepository.save(any())).thenReturn(mockLabel);

        Label createdLabel = labelService.createLabel(mockLabel);

        assertNotNull(createdLabel);
        assertEquals("1", createdLabel.getCode());
        assertEquals("Mock Label", createdLabel.getDescription());
    }

    @Test
    public void testGetLabelById_ExistingLabel() {
        when(labelRepository.findById("1")).thenReturn(Optional.of(mockLabel));

        Optional<Label> result = labelService.getLabelById("1");

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getCode());
        assertEquals("Mock Label", result.get().getDescription());
    }

    @Test
    public void testGetLabelById_NonExistingLabel() {
        when(labelRepository.findById("2")).thenReturn(Optional.empty());

        Optional<Label> result = labelService.getLabelById("2");

        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateLabel_ExistingLabel() {
        when(labelRepository.findById("1")).thenReturn(Optional.of(mockLabel));
        when(labelRepository.save(any())).thenReturn(mockLabel);

        Label updatedLabel = new Label("1", "Updated Label");
        Optional<Label> result = labelService.updateLabel("1", updatedLabel);

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getCode());
        assertEquals("Updated Label", result.get().getDescription());
    }

    @Test
    public void testUpdateLabel_NonExistingLabel() {
        when(labelRepository.findById("2")).thenReturn(Optional.empty());

        Label updatedLabel = new Label("2", "Updated Label");
        Optional<Label> result = labelService.updateLabel("2", updatedLabel);

        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteLabel_ExistingLabel() {
        when(labelRepository.findById("1")).thenReturn(Optional.of(mockLabel));

        boolean deleted = labelService.deleteLabel("1");

        assertTrue(deleted);
        verify(labelRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteLabel_NonExistingLabel() {
        when(labelRepository.findById("2")).thenReturn(Optional.empty());

        boolean deleted = labelService.deleteLabel("2");

        assertFalse(deleted);
        verify(labelRepository, never()).deleteById("2");
    }

    @Test
    public void testGetAllLabels() {
        List<Label> mockLabels = new ArrayList<>();
        mockLabels.add(new Label("1", "Mock Label 1"));
        mockLabels.add(new Label("2", "Mock Label 2"));

        when(labelRepository.findAll()).thenReturn(mockLabels);

        List<Label> labels = labelService.getAllLabels();

        assertEquals(2, labels.size());
    }
}
