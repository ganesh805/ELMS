package com.elms.service;

import com.elms.dto.request.LeaveTypeCreateDTO;
import com.elms.dto.request.LeaveTypeUpdateDTO;
import com.elms.dto.response.LeaveTypeDTO;
import com.elms.entity.LeaveType;
import com.elms.exception.BusinessRuleException;
import com.elms.exception.ResourceNotFoundException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional(readOnly = true)
    public List<LeaveTypeDTO> getAllLeaveTypes() {
        return leaveTypeRepository.findAll().stream()
                .map(EntityMapper::toLeaveTypeDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveTypeDTO> getAllActiveLeaveTypes() {
        return leaveTypeRepository.findByActiveTrue().stream()
                .map(EntityMapper::toLeaveTypeDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveTypeDTO> getActiveLeaveTypes() {
        return getAllActiveLeaveTypes();
    }

    @Transactional
    public LeaveTypeDTO createLeaveType(LeaveTypeCreateDTO dto) {
        if (leaveTypeRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BusinessRuleException("Leave type with name '" + dto.getName() + "' already exists");
        }

        LeaveType leaveType = LeaveType.builder()
                .name(dto.getName())
                .defaultAnnualQuota(dto.getDefaultAnnualQuota())
                .description(dto.getDescription())
                .requiresApproval(dto.getRequiresApproval() != null ? dto.getRequiresApproval() : true)
                .active(true)
                .build();

        LeaveType saved = leaveTypeRepository.save(leaveType);
        return EntityMapper.toLeaveTypeDTO(saved);
    }

    @Transactional
    public LeaveTypeDTO updateLeaveType(Long id, LeaveTypeUpdateDTO dto) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            leaveType.setName(dto.getName());
        }
        if (dto.getDefaultAnnualQuota() != null) {
            leaveType.setDefaultAnnualQuota(dto.getDefaultAnnualQuota());
        }
        if (dto.getDescription() != null) {
            leaveType.setDescription(dto.getDescription());
        }
        if (dto.getRequiresApproval() != null) {
            leaveType.setRequiresApproval(dto.getRequiresApproval());
        }
        if (dto.getActive() != null) {
            leaveType.setActive(dto.getActive());
        }

        LeaveType updated = leaveTypeRepository.save(leaveType);
        return EntityMapper.toLeaveTypeDTO(updated);
    }

    @Transactional
    public void deleteLeaveType(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + id));

        leaveType.setActive(false);
        leaveTypeRepository.save(leaveType);
    }
}
