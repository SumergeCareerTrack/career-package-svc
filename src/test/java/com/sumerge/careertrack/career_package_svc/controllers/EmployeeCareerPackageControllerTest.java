package com.sumerge.careertrack.career_package_svc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import com.sumerge.careertrack.career_package_svc.exceptions.DoesNotExistException;
import com.sumerge.careertrack.career_package_svc.exceptions.GlobalExceptionHandler;
import com.sumerge.careertrack.career_package_svc.services.EmployeeCareerPackageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = EmployeeCareerPackageController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = EmployeeCareerPackageController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeCareerPackageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeCareerPackageService employeeCareerPackageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployeeCareerPackages_without_Pagination() throws Exception {
        List<EmployeeCareerPackageResponseDTO> employeeCareerPackageResponseDTOList = new ArrayList<>();
        employeeCareerPackageResponseDTOList.add(new EmployeeCareerPackageResponseDTO());
        when(employeeCareerPackageService.getAllEmployeeCareerPackages()).thenReturn(employeeCareerPackageResponseDTOList);

        mockMvc.perform(get("/employee-packages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).getAllEmployeeCareerPackages();
    }

    @Test
    void getAllEmployeeCareerPackages_with_Pagination() throws Exception {
        List<EmployeeCareerPackageResponseDTO> response = Collections.singletonList(new EmployeeCareerPackageResponseDTO());
        when(employeeCareerPackageService.getAllEmployeeCareerPackages(any(Pageable.class))).thenReturn(response);

        mockMvc.perform(get("/employee-packages").param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))); // Adjust based on expected response
    }

    @Test
    void getAllEmployeeCareerPackages_Not_Found() throws Exception {
        List<EmployeeCareerPackageResponseDTO> response = Collections.emptyList();
        when(employeeCareerPackageService.getAllEmployeeCareerPackages()).thenReturn(response);
        mockMvc.perform(get("/employee-packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(employeeCareerPackageService, times(1)).getAllEmployeeCareerPackages();
    }

    @Test
    void getAllEmployeeCareerPackageById_Successful() throws Exception {
        UUID id = UUID.randomUUID();
        EmployeeCareerPackageResponseDTO resp = new EmployeeCareerPackageResponseDTO();
        resp.setId(id);
        when(employeeCareerPackageService.getEmployeeCareerPackageByPackageId(id)).thenReturn(resp);
        mockMvc.perform(get("/employee-packages/" + id))
                .andExpect(status().isOk());
        verify(employeeCareerPackageService, times(1)).getEmployeeCareerPackageByPackageId(id);
    }

    @Test
    void getAllEmployeeCareerPackageById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeCareerPackageService.getEmployeeCareerPackageByPackageId(id))
                .thenThrow(DoesNotExistException.class);
        mockMvc.perform(get("/employee-packages/" + id))
                .andExpect(status().isNotFound());

        verify(employeeCareerPackageService, times(1)).getEmployeeCareerPackageByPackageId(id);
    }


     @Test
    void getAllEmployeeCareerPackageByEmployeeId_Successful() throws Exception {
        UUID id = UUID.randomUUID();
        List<EmployeeCareerPackageResponseDTO> respList = new ArrayList<>();
        respList.add(new EmployeeCareerPackageResponseDTO());
        when(employeeCareerPackageService.getEmployeeCareerPackageByEmployeeId(id)).thenReturn(respList);
        mockMvc.perform(get("/employee-packages/employee/" + id))
                .andExpect(status().isOk());
        verify(employeeCareerPackageService, times(1)).getEmployeeCareerPackageByEmployeeId(id);
    }

    @Test
    void getAllEmployeeCareerPackageByEmployeeId_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeCareerPackageService.getEmployeeCareerPackageByEmployeeId(id))
                .thenThrow(DoesNotExistException.class);
        mockMvc.perform(get("/employee-packages/employee/" + id))
                .andExpect(status().isNotFound());
        verify(employeeCareerPackageService, times(1)).getEmployeeCareerPackageByEmployeeId(id);
    }


    @Test
    void getAllSubordinateEmployeeCareerPackages_Successful() throws Exception {
        UUID manager_id = UUID.randomUUID();
        List<UUID> idList = new ArrayList<>();
        idList.add(UUID.randomUUID());
        idList.add(UUID.randomUUID());
        List<EmployeeCareerPackageResponseDTO> empList = new ArrayList<>();
        when(employeeCareerPackageService.getAllSubordinateEmployeeCareerPackages(idList))
                .thenReturn(empList);

        mockMvc.perform(post("/employee-packages/manager/"+manager_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idList)))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).getAllSubordinateEmployeeCareerPackages(idList);
    }

    @Test
    void getAllSubordinateEmployeeCareerPackages_NotFound() throws Exception {
        UUID manager_id = UUID.randomUUID();
        List<UUID> idList = new ArrayList<>();
        when(employeeCareerPackageService.getAllSubordinateEmployeeCareerPackages(idList))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/employee-packages/manager/"+manager_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(employeeCareerPackageService, times(1)).getAllSubordinateEmployeeCareerPackages(idList);
    }

    @Test
    void deleteEmployeeCareerPackageById_Successful() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeCareerPackageService.deleteEmployeeCareerPackage(id))
                .thenReturn("successful");
        mockMvc.perform(delete("/employee-packages/" + id))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).deleteEmployeeCareerPackage(id);
    }

    @Test
    void deleteEmployeeCareerPackageById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(employeeCareerPackageService.deleteEmployeeCareerPackage(id))
        .thenThrow(DoesNotExistException.class);

        mockMvc.perform(delete("/employee-packages/" + id))
                .andExpect(status().isNotFound());
        verify(employeeCareerPackageService, times(1)).deleteEmployeeCareerPackage(id);
    }

    @Test
    void createEmployeeCareerPackage_Success() throws Exception {
        UUID managerId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        EmployeeCareerPackageRequestDTO req = new EmployeeCareerPackageRequestDTO();
        req.setEmployeeId(UUID.randomUUID());
        req.setFile(file);
        req.setTitle(UUID.randomUUID().toString());

        when(employeeCareerPackageService.createEmployeeCareerPackage(any(EmployeeCareerPackageRequestDTO.class), eq(managerId.toString())))
                .thenReturn(new EmployeeCareerPackageResponseDTO());

        mockMvc.perform(multipart("/employee-packages/" + managerId)
                        .file(file)
                        .param("employeeId", UUID.randomUUID().toString())
                        .param("name", "Employee Name")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).createEmployeeCareerPackage(any(EmployeeCareerPackageRequestDTO.class), eq(managerId.toString()));
    }

    @Test
    void createEmployeeCareerPackage_Not_Successful() throws Exception {
        UUID managerId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());

        EmployeeCareerPackageRequestDTO req = new EmployeeCareerPackageRequestDTO();
        req.setEmployeeId(UUID.randomUUID());
        req.setFile(file);
        req.setTitle(UUID.randomUUID().toString());

        when(employeeCareerPackageService.createEmployeeCareerPackage(any(EmployeeCareerPackageRequestDTO.class), eq(managerId.toString())))
                .thenThrow(DoesNotExistException.class);

        mockMvc.perform(multipart("/employee-packages/" + managerId)
                        .file(file)
                        .param("employeeId", UUID.randomUUID().toString())
                        .param("name", "Employee Name")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound());

        verify(employeeCareerPackageService, times(1)).createEmployeeCareerPackage(any(EmployeeCareerPackageRequestDTO.class), eq(managerId.toString()));
    }

    @Test
    void updateEmployeeCareerPackage_Success() throws Exception {
        UUID employeePackageId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "updated content".getBytes());

        EmployeeCareerPackageRequestDTO req = new EmployeeCareerPackageRequestDTO();
        req.setEmployeeId(employeePackageId);
        req.setFile(file);
        req.setTitle("Updated Name");

        when(employeeCareerPackageService.updateEmployeeCareerPackage(eq(employeePackageId), any(EmployeeCareerPackageRequestDTO.class)))
                .thenReturn(new EmployeeCareerPackageResponseDTO());

        mockMvc.perform(multipart("/employee-packages/" + employeePackageId)
                        .file(file)
                        .param("name", "Updated Name")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).updateEmployeeCareerPackage(eq(employeePackageId), any(EmployeeCareerPackageRequestDTO.class));
    }

    @Test
    void updateEmployeeCareerPackage_Not_Successful() throws Exception {
        UUID employeePackageId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "updated content".getBytes());

        EmployeeCareerPackageRequestDTO req = new EmployeeCareerPackageRequestDTO();
        req.setEmployeeId(employeePackageId);
        req.setFile(file);
        req.setTitle("Updated Name");

        when(employeeCareerPackageService.updateEmployeeCareerPackage(eq(employeePackageId), any(EmployeeCareerPackageRequestDTO.class)))
                .thenThrow(DoesNotExistException.class);

        mockMvc.perform(multipart("/employee-packages/" + employeePackageId)
                        .file(file)
                        .param("name", "Updated Name")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT"); // Explicitly setting the method to PUT
                            return request;
                        }))
                .andExpect(status().isNotFound());

        verify(employeeCareerPackageService, times(1)).updateEmployeeCareerPackage(eq(employeePackageId), any(EmployeeCareerPackageRequestDTO.class));
    }

    @Test
    void approveEmployeeCareerPackageById_Successful() throws Exception {
        UUID package_id = UUID.randomUUID();
        String manager_id = UUID.randomUUID().toString();
        String comment = "comment";

        EmployeeCareerPackageResponseDTO resp = new EmployeeCareerPackageResponseDTO();
        resp.setId(package_id);
        when(employeeCareerPackageService.approveEmployeeCareerPackage(package_id,comment,manager_id))
                .thenReturn(resp);

        mockMvc.perform(put("/employee-packages/" + package_id + "/approve/" + manager_id)
                        .content(comment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).approveEmployeeCareerPackage(package_id,comment,manager_id);
    }

    @Test
    void approveEmployeeCareerPackageById_NotFound() throws Exception {
        UUID package_id = UUID.randomUUID();
        String manager_id = UUID.randomUUID().toString();
        String comment = "comment";

        when(employeeCareerPackageService.approveEmployeeCareerPackage(package_id,comment,manager_id))
                .thenThrow(DoesNotExistException.class);

        mockMvc.perform(put("/employee-packages/" + package_id + "/approve/" + manager_id)
                        .content(comment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(employeeCareerPackageService, times(1)).approveEmployeeCareerPackage(package_id,comment,manager_id);
    }

    @Test
    void rejectEmployeeCareerPackageById_Successful() throws Exception {
        UUID package_id = UUID.randomUUID();
        String manager_id = UUID.randomUUID().toString();
        String comment = "comment";

        EmployeeCareerPackageResponseDTO resp = new EmployeeCareerPackageResponseDTO();
        resp.setId(package_id);
        when(employeeCareerPackageService.rejectEmployeeCareerPackage(package_id,comment,manager_id))
                .thenReturn(resp);

        mockMvc.perform(put("/employee-packages/" + package_id + "/reject/" + manager_id)
                        .content(comment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeCareerPackageService, times(1)).rejectEmployeeCareerPackage(package_id,comment,manager_id);
    }

    @Test
    void rejectEmployeeCareerPackageById_NotFound() throws Exception {
        UUID package_id = UUID.randomUUID();
        String manager_id = UUID.randomUUID().toString();
        String comment = "comment";

        when(employeeCareerPackageService.rejectEmployeeCareerPackage(package_id,comment,manager_id))
                .thenThrow(DoesNotExistException.class);

        mockMvc.perform(put("/employee-packages/" + package_id + "/reject/" + manager_id)
                        .content(comment)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(employeeCareerPackageService, times(1)).rejectEmployeeCareerPackage(package_id,comment,manager_id);
    }

}