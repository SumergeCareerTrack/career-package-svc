package com.sumerge.careertrack.career_package_svc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.careertrack.career_package_svc.controllers.EmployeeCareerPackageController;
import com.sumerge.careertrack.career_package_svc.entities.EmployeeCareerPackage;
import com.sumerge.careertrack.career_package_svc.mappers.EmployeeCareerPackageMapper;
import com.sumerge.careertrack.career_package_svc.repositories.EmployeeCareerPackageRepository;
import com.sumerge.careertrack.career_package_svc.services.EmployeeCareerPackageService;
import com.sumerge.careertrack.career_package_svc.services.FileService;
import com.sumerge.careertrack.career_package_svc.services.ProducerService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeCareerPackageIntegerationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    EmployeeCareerPackageController controller;

    @Autowired
    EmployeeCareerPackageService employeeCareerPackageService;

    @Autowired
    EmployeeCareerPackageRepository repo;

    @Autowired
    EmployeeCareerPackageMapper mapper;

    @Autowired
    FileService fileService;

    @Autowired
    ProducerService producerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    EmployeeCareerPackage employeeCareerPackage;
    @BeforeEach
    public void setUp() throws Exception {
        employeeCareerPackage = new EmployeeCareerPackage();
        employeeCareerPackage.setId(UUID.randomUUID());
        employeeCareerPackage.setEmployeeId(UUID.randomUUID());
        employeeCareerPackage.setFileId(UUID.randomUUID().toString());
        employeeCareerPackage.setSubmissionDate(new Date());
        employeeCareerPackage.setTitle(UUID.randomUUID().toString());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllEmployeeCareerPackages() throws Exception {
        List<EmployeeCareerPackage> emp =  repo.findAll();
        mockMvc.perform(get("/employee-packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(emp.size())));
    }

    @Test
    void addEmployeeCareerPackage() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        UUID employeeId = UUID.randomUUID();
        String managerId = UUID.randomUUID().toString();
        String name = "Test Career Package";

        String response = mockMvc.perform(multipart("/employee-packages/" + managerId)
                        .file(file)
                        .param("employeeId", employeeId.toString())
                        .param("name", name)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        String packageId = jsonResponse.get("id").asText();

        mockMvc.perform(delete("/employee-packages/" + packageId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void updateEmployeeCareerPackage() throws Exception {
       EmployeeCareerPackage emp = repo.save(employeeCareerPackage);
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
       mockMvc.perform(multipart("/employee-packages/" + emp.getId())
                       .file(file)
                       .param("name" , "not-name")
                       .with(request -> {
                           request.setMethod("PUT");
                           return request;
                       }))
               .andExpect(status().isOk());
       repo.deleteById(emp.getId());
    }

    @Test
    void deletePackageById_Exist() throws Exception {
        EmployeeCareerPackage emp =  repo.save(employeeCareerPackage);
        UUID id = emp.getId();

        mockMvc.perform(get("/employee-packages/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/employee-packages/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employee-packages/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void deletePackageById_DoesNotExist() throws Exception {
        mockMvc.perform(delete("/employee-packages/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeeCareerPackageById_Exist() throws Exception {
        EmployeeCareerPackage emp =  repo.save(employeeCareerPackage);
        UUID id = emp.getId();
        mockMvc.perform(get("/employee-packages/" + id))
                .andExpect(status().isOk());
        repo.delete(employeeCareerPackage);
    }

    @Test
    void getEmployeeCareerPackageById_DoesNotExist() throws Exception {
        mockMvc.perform(get("/employee-packages/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void approveEmployeeCareerPackage_Successful() throws Exception {
        EmployeeCareerPackage emp =  repo.save(employeeCareerPackage);
        UUID id = emp.getId();

        mockMvc.perform(put("/employee-packages/" + id + "/approve/"+ UUID.randomUUID())
                        .content("Great Work !"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approvalStatus").value("APPROVED"));

        repo.delete(employeeCareerPackage);
    }

    @Test
    void approveEmployeeCareerPackage_Not_Successful() throws Exception {
        mockMvc.perform(put("/employee-packages/" + UUID.randomUUID() + "/approve/"+ UUID.randomUUID())
                        .content("Great Work !"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectEmployeeCareerPackage_Successful() throws Exception {
        EmployeeCareerPackage emp =  repo.save(employeeCareerPackage);
        UUID id = emp.getId();

        mockMvc.perform(put("/employee-packages/" + id + "/reject/"+ UUID.randomUUID())
                        .content("Bad Work !"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approvalStatus").value("REJECTED"));

        repo.delete(employeeCareerPackage);
    }

    @Test
    void rejectEmployeeCareerPackage_Not_Successful() throws Exception {

        mockMvc.perform(put("/employee-packages/" + UUID.randomUUID() + "/reject/"+ UUID.randomUUID())
                        .content("Bad Work !"))
                .andExpect(status().isNotFound());
    }

}
