package com.elms.service;

import com.elms.dto.response.LeaveTypeDTO;
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
    public List<LeaveTypeDTO> getAllActiveLeaveTypes() {
        return leaveTypeRepository.findByActiveTrue().stream()
                .map(EntityMapper::toLeaveTypeDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveTypeDTO> getAllLeaveTypes() {
        return leaveTypeRepository.findAll().stream()
                .map(EntityMapper::toLeaveTypeDTO)
                .toList();
    }
}
