package com.sumerge.careertrack.career_package_svc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import com.sumerge.careertrack.career_package_svc.exceptions.DoesNotExistException;
import com.sumerge.careertrack.career_package_svc.services.EmployeeCareerPackageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmployeeCareerPackageController.class)
@ExtendWith(MockitoExtension.class)
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

//
//    @Test
//    void updateEmployeeCareerPackageById_Successful() throws Exception {
//        UUID id = UUID.randomUUID();
//        EmployeeCareerPackageResponseDTO resp = new EmployeeCareerPackageResponseDTO();
//        resp.setId(id);
//
//    }

//    @Test
//    void updateEmployeeCareerPackageById_NotFound() throws Exception {
//        UUID id = UUID.randomUUID();
//
//        String fileContent = "test"; // Your file content
//        String encodedFile = Base64.getEncoder().encodeToString(fileContent.getBytes(StandardCharsets.UTF_8));
//        EmployeeCareerPackageRequestDTO req = new EmployeeCareerPackageRequestDTO();
//        when(employeeCareerPackageService.updateEmployeeCareerPackage(id,req))
//                .thenThrow(DoesNotExistException.class);
//
//        mockMvc.perform(multipart("/employee-packages/" + id)
//                        .param("name" , "name")
//                        .param("file" , encodedFile)
//                .contentType(MediaType.MULTIPART_FORM_DATA)) // Set content type
//                .andExpect(status().isNotFound());
//
//        verify(employeeCareerPackageService, times(1)).updateEmployeeCareerPackage(id,req);
//
//    }

//    @Test
//    void testUpdateEmployeeCareerPackage() throws Exception {
//        UUID employeePackageId = UUID.randomUUID();
//        String name = "Updated Package";
//        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
//        EmployeeCareerPackageRequestDTO req = new EmployeeCareerPackageRequestDTO();
//        req.setEmployeeId(employeePackageId);
//
//        EmployeeCareerPackageResponseDTO responseDTO = new EmployeeCareerPackageResponseDTO();
//        responseDTO.setEmployeeId(employeePackageId);
//        when(employeeCareerPackageService.updateEmployeeCareerPackage(employeePackageId,req))
//                .thenReturn(responseDTO);
//
//        mockMvc.perform(multipart("/employeePackage/" + employeePackageId)
//                        .file(file)
//                        .param("name", name)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk());
//    }

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