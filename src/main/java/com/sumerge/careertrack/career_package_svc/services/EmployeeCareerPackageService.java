package com.sumerge.careertrack.career_package_svc.services;

import com.sumerge.careertrack.career_package_svc.entities.EmployeeCareerPackage;
import com.sumerge.careertrack.career_package_svc.entities.enums.ActionEnum;
import com.sumerge.careertrack.career_package_svc.entities.enums.ApprovalStatus;
import com.sumerge.careertrack.career_package_svc.entities.enums.EntityTypeEnum;
import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.requests.NotificationRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import com.sumerge.careertrack.career_package_svc.exceptions.DoesNotExistException;
import com.sumerge.careertrack.career_package_svc.mappers.EmployeeCareerPackageMapper;
import com.sumerge.careertrack.career_package_svc.repositories.EmployeeCareerPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeCareerPackageService {

    private final EmployeeCareerPackageRepository employeeCareerPackageRepository;
    private final EmployeeCareerPackageMapper employeeCareerPackageMapper;
    private final FileService fileService;
    public final ProducerService producerService;

    public List<EmployeeCareerPackageResponseDTO> getAllEmployeeCareerPackages() {
        List<EmployeeCareerPackage> employeeCareerPackages = employeeCareerPackageRepository.findAll();
        return employeeCareerPackages.stream().map(employeeCareerPackageMapper::toEmployeeCareerPackageResponseDTO).collect(Collectors.toList());
    }
    public List<EmployeeCareerPackageResponseDTO> getAllEmployeeCareerPackages(Pageable pageable) {
        Page<EmployeeCareerPackage> paginatedEmployeeCareerPackages = employeeCareerPackageRepository.findAll(pageable);
        return paginatedEmployeeCareerPackages.getContent().stream()
                .map(employeeCareerPackageMapper::toEmployeeCareerPackageResponseDTO)
                .collect(Collectors.toList());
    }

    public EmployeeCareerPackageResponseDTO getEmployeeCareerPackageByPackageId(UUID id) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , id));

        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackage);
    }

    public List<EmployeeCareerPackageResponseDTO> getEmployeeCareerPackageByEmployeeId(UUID id) {
        List<EmployeeCareerPackage> employeeCareerPackages = employeeCareerPackageRepository.findAllByEmployeeIdOrderBySubmissionDateAsc(id);
        return employeeCareerPackageMapper.toResponseDTOList(employeeCareerPackages);
    }

    public EmployeeCareerPackageResponseDTO createEmployeeCareerPackage(EmployeeCareerPackageRequestDTO employeeCareerPackageRequestDTO,String id) throws Exception {
        UUID managerId = UUID.fromString(id);
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageMapper.toEmployeeCareerPackage(employeeCareerPackageRequestDTO);
        employeeCareerPackage.setFileId(fileService.addFile(employeeCareerPackageRequestDTO.getFile()));
        employeeCareerPackage.setSubmissionDate(new Date());
        List<UUID> receiverId = new ArrayList<UUID>();
        receiverId.add(managerId);
        EmployeeCareerPackage saved = employeeCareerPackageRepository.save(employeeCareerPackage);
        NotificationRequestDTO notification=createNotification(saved,receiverId,ActionEnum.SUBMISSION,employeeCareerPackage.getEmployeeId(),saved.getSubmissionDate());
        producerService.sendMessage(notification);
        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(saved);
    }


    public EmployeeCareerPackageResponseDTO updateEmployeeCareerPackage(UUID packageId,  EmployeeCareerPackageRequestDTO employeeCareerPackageRequestDTO) throws Exception {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId));

        employeeCareerPackage.setFileId(fileService.addFile(employeeCareerPackageRequestDTO.getFile()));
        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }

    public String deleteEmployeeCareerPackage(UUID packageId) {
        if (!employeeCareerPackageRepository.existsById(packageId)) {
            throw new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId);
        }
        else employeeCareerPackageRepository.deleteById(packageId);

        return "Deleted Successfully";
    }

    public EmployeeCareerPackageResponseDTO approveEmployeeCareerPackage(UUID packageId , String comment,String id) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId));

        employeeCareerPackage.setApprovalStatus(ApprovalStatus.APPROVED);
        employeeCareerPackage.setComment(comment);
        UUID managerId = UUID.fromString(id);
        List<UUID> receiverId = new ArrayList<>();
        receiverId.add(employeeCareerPackage.getEmployeeId());
        NotificationRequestDTO notification=createNotification(employeeCareerPackage,receiverId,ActionEnum.APPROVAL,managerId,new Date());
        producerService.sendMessage(notification);
        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }

    public EmployeeCareerPackageResponseDTO rejectEmployeeCareerPackage(UUID packageId , String comment,String id)  {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId));

        employeeCareerPackage.setApprovalStatus(ApprovalStatus.REJECTED);
        employeeCareerPackage.setComment(comment);

        UUID managerId = UUID.fromString(id);
        List<UUID> receiverId = new ArrayList<>();
        receiverId.add(employeeCareerPackage.getEmployeeId());

        NotificationRequestDTO notification=createNotification(employeeCareerPackage,receiverId,ActionEnum.REJECTION,managerId,new Date());
        producerService.sendMessage(notification);
        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }

    public List<EmployeeCareerPackageResponseDTO> getAllSubordinateEmployeeCareerPackages(List<UUID> employeeIds){
      List<EmployeeCareerPackage> careerPackages =  employeeCareerPackageRepository.findLatestSubmissionsByEmployeeIds(employeeIds);
      return employeeCareerPackageMapper.toResponseDTOList(careerPackages);
    }
    public NotificationRequestDTO createNotification(EmployeeCareerPackage savedArticle, List<UUID> receiverId, ActionEnum actionEnum,UUID actorId,Date date) {
        return NotificationRequestDTO.builder()
                .seen(false)
                .date(date)
                .actorId(actorId)
                .entityId(savedArticle.getId())
                .actionName(actionEnum)
                .entityTypeName(EntityTypeEnum.CAREER_PACKAGE)
                .receiverID(receiverId)
                .build();
    }
}
