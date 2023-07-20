package com.cocus.doctorLablling.integrationTest;

import com.cocus.doctorLablling.model.Case;
import com.cocus.doctorLablling.model.Label;
import com.cocus.doctorLablling.repository.CaseRepository;
import com.cocus.doctorLablling.repository.LabelRepository;
import com.cocus.doctorLablling.service.CaseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CaseControllerIntegrationTest{


    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private CaseServiceImpl caseService;

    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    public void setUps() {
        // Clear the repositories before each test
        caseRepository.deleteAll();
        labelRepository.deleteAll();
    }

    @Test
    public void testCreateCase() throws Exception {
        Case caseObj = new Case();
        caseObj.setCaseId("123");
        caseObj.setCaseDescription("Test Case");
        caseObj.setDoctorId(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(caseObj)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        List<Case> cases = caseRepository.findAll();
        assertEquals(1, cases.size());
        assertEquals("123", cases.get(0).getCaseId());
        assertEquals("Test Case", cases.get(0).getCaseDescription());
        assertEquals(1L, cases.get(0).getDoctorId());
    }

    @Test
    @Transactional
    public void testCreateNewLabelInCase() throws Exception {
        Case caseObj = new Case();
        caseObj.setCaseId("123");
        caseObj.setCaseDescription("Test Case");
        caseObj.setDoctorId(1L);

        Label label = new Label();
        label.setCode("A001");
        label.setDescription("Test Label");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(caseObj)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cases/123/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(label)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Case> cases = caseRepository.findAll();
        assertEquals(1, cases.size());
        assertEquals("123", cases.get(0).getCaseId());
        assertEquals(1, cases.get(0).getLabels().size());
        assertEquals("A001", cases.get(0).getLabels().get(0).getCode());
        assertEquals("Test Label", cases.get(0).getLabels().get(0).getDescription());
    }

    @Test
    public void testGetAllCases() throws Exception {
        Case case1 = new Case("123", "Test Case 1", 1L, new ArrayList<>(), LocalDateTime.now());
        Case case2 = new Case("456", "Test Case 2", 2L, new ArrayList<>(), LocalDateTime.now());

        caseRepository.saveAll(Arrays.asList(case1, case2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cases"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].caseId").value("123"))
                .andExpect(jsonPath("$[0].caseDescription").value("Test Case 1"))
                .andExpect(jsonPath("$[0].doctorId").value(1))
                .andExpect(jsonPath("$[1].caseId").value("456"))
                .andExpect(jsonPath("$[1].caseDescription").value("Test Case 2"))
                .andExpect(jsonPath("$[1].doctorId").value(2));
    }

    @Test
    public void testGetCaseById() throws Exception {
        Case caseObj = new Case("123", "Test Case", 1L, new ArrayList<>(), LocalDateTime.now());
        caseRepository.save(caseObj);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cases/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.caseId").value("123"))
                .andExpect(jsonPath("$.caseDescription").value("Test Case"))
                .andExpect(jsonPath("$.doctorId").value(1));
    }

    @Test
    public void testGetCaseByInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cases/invalid-id"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetCasesByLabelCode() throws Exception {
        Case case1 = new Case("123", "Test Case 1", 1L, new ArrayList<>(), LocalDateTime.now());
        Case case2 = new Case("456", "Test Case 2", 2L, new ArrayList<>(), LocalDateTime.now());

        Label label1 = new Label("A001", "Label 1");
        Label label2 = new Label("B002", "Label 2");
        case1.getLabels().add(label1);
        case2.getLabels().add(label2);

        caseRepository.saveAll(Arrays.asList(case1, case2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cases")
                        .param("label", "A001"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].caseId").value("123"))
                .andExpect(jsonPath("$[0].caseDescription").value("Test Case 1"))
                .andExpect(jsonPath("$[0].doctorId").value(1));
    }

    @Test
    public void testDeleteLabelInCase() throws Exception {
        Case caseObj = new Case("123", "Test Case", 1L, new ArrayList<>(), LocalDateTime.now());
        Label label = new Label("A001", "Label 1");
        caseObj.getLabels().add(label);

        caseRepository.save(caseObj);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/cases/123/labels/A001"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        List<Case> cases = caseRepository.findAll();
        assertEquals(1, cases.size());
        assertEquals(0, cases.get(0).getLabels().size());
    }

   /* @Test
    public void testUpdateCase() throws Exception {
        Case caseObj = new Case("123", "Test Case", 1L, new ArrayList<>(), LocalDateTime.now());
        caseRepository.save(caseObj);

        Case updatedCase = new Case("123", "Updated Test Case", 2L, new ArrayList<>(), LocalDateTime.now());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cases/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCase)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Case> cases = caseRepository.findAll();
        assertEquals(1, cases.size());
        assertEquals("Updated Test Case", cases.get(0).getCaseDescription());
        assertEquals(2L, cases.get(0).getDoctorId());
    }*/
    @Test
    public void testUpdateCase() throws Exception {
        Case caseObj = new Case("123", "Test Case", 1L, new ArrayList<>(), LocalDateTime.now());
        caseRepository.save(caseObj);

        Case updatedCase = new Case("123", "Updated Test Case", 2L, new ArrayList<>(), LocalDateTime.now());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cases/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCase)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Case> cases = caseRepository.findAll();
        assertEquals(1, cases.size());
        assertEquals("Updated Test Case", cases.get(0).getCaseDescription());
        assertEquals(2L, cases.get(0).getDoctorId());
    }

    // Helper method to convert Java objects to JSON
    private static String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
