package com.cocus.doctorLablling.integrationTest;

import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.repository.LabelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test
        labelRepository.deleteAll();
    }

    @Test
    public void testCreateLabel() throws Exception {
        Label label = new Label();
        label.setCode("A001");
        label.setDescription("Test Label");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(label)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        List<Label> labels = labelRepository.findAll();
        assertEquals(1, labels.size());
        assertEquals("A001", labels.get(0).getCode());
        assertEquals("Test Label", labels.get(0).getDescription());
    }

    @Test
    public void testGetAllLabels() throws Exception {
        Label label1 = new Label("A001", "Label 1");
        Label label2 = new Label("B002", "Label 2");

        labelRepository.saveAll(List.of(label1, label2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/labels"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("A001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Label 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].code").value("B002"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Label 2"));
    }

    @Test
    public void testGetLabelById() throws Exception {
        Label label = new Label("A001", "Test Label");
        labelRepository.save(label);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/labels/A001"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("A001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Label"));
    }

    @Test
    public void testGetLabelByInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/labels/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateLabel() throws Exception {
        Label label = new Label("A001", "Test Label");
        labelRepository.save(label);

        Label updatedLabel = new Label("A001", "Updated Test Label");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/labels/A001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedLabel)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Label> labels = labelRepository.findAll();
        assertEquals(1, labels.size());
        assertEquals("Updated Test Label", labels.get(0).getDescription());
    }

    @Test
    public void testDeleteLabel() throws Exception {
        Label label = new Label("A001", "Test Label");
        labelRepository.save(label);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/labels/A001"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        List<Label> labels = labelRepository.findAll();
        assertEquals(0, labels.size());
    }

    // Helper method to convert Java objects to JSON
    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

