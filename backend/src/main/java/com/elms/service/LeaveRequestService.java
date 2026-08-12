package com.elms.service;

import com.elms.dto.request.LeaveCreateDTO;
import com.elms.dto.response.LeaveRequestDTO;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveRequest;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.entity.enums.LeaveStatus;
import com.elms.exception.BusinessRuleException;
import com.elms.exception.InsufficientLeaveBalanceException;
import com.elms.exception.InvalidLeaveStateException;
import com.elms.exception.ResourceNotFoundException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveRequestRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final WorkingDayService workingDayService;

    @Transactional
    public LeaveRequestDTO createLeaveRequest(Long userId, LeaveCreateDTO dto, String attachmentFileName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + dto.getLeaveTypeId()));

        if (!Boolean.TRUE.equals(leaveType.getActive())) {
            throw new BusinessRuleException("Selected leave type is inactive");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new BusinessRuleException("End date cannot be before start date");
        }

        if (dto.getStartDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Start date cannot be in the past");
        }

        List<LeaveStatus> activeStatuses = List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED);
        List<LeaveRequest> overlappingRequests = leaveRequestRepository.findOverlappingRequests(
                userId, dto.getStartDate(), dto.getEndDate(), activeStatuses);

        if (!overlappingRequests.isEmpty()) {
            throw new BusinessRuleException("Selected leave dates overlap with an existing PENDING or APPROVED leave request");
        }

        int workingDays = workingDayService.calculateWorkingDays(dto.getStartDate(), dto.getEndDate());
        if (workingDays == 0) {
            throw new BusinessRuleException("Selected date range contains no working days (weekends or public holidays)");
        }

        int leaveYear = dto.getStartDate().getYear();
        LeaveBalance balance = leaveBalanceRepository.findByUserIdAndLeaveTypeIdAndYear(userId, dto.getLeaveTypeId(), leaveYear)
                .orElseThrow(() -> new BusinessRuleException("No leave balance allocated for this leave type in year " + leaveYear));

        if (workingDays > balance.getRemaining()) {
            throw new InsufficientLeaveBalanceException(
                    String.format("Requested leave duration (%d days) exceeds remaining balance (%d days)",
                            workingDays, balance.getRemaining()));
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .user(user)
                .leaveType(leaveType)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .numberOfDays(workingDays)
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .attachmentFileName(attachmentFileName)
                .appliedOn(LocalDateTime.now())
                .build();

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return EntityMapper.toLeaveRequestDTO(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestDTO> getMyLeaveRequests(Long userId, LeaveStatus status) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        if (status == null) {
            return leaveRequestRepository.findByUserId(userId).stream()
                    .map(EntityMapper::toLeaveRequestDTO)
                    .toList();
        }
        return leaveRequestRepository.findByUserIdAndStatus(userId, status).stream()
                .map(EntityMapper::toLeaveRequestDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestDTO> getPendingApprovalsForManager(Long managerId) {
        if (!userRepository.existsById(managerId)) {
            throw new ResourceNotFoundException("Manager not found with id: " + managerId);
        }
        return leaveRequestRepository.findByUserManagerIdAndStatus(managerId, LeaveStatus.PENDING).stream()
                .map(EntityMapper::toLeaveRequestDTO)
                .toList();
    }

    @Transactional
    public LeaveRequestDTO approveLeaveRequest(Long requestId, Long approverId, String decisionComment) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStateException("Only PENDING leave requests can be approved");
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver user not found with id: " + approverId));

        request.setStatus(LeaveStatus.APPROVED);
        request.setApprover(approver);
        request.setDecisionComment(decisionComment);
        request.setDecisionDate(LocalDateTime.now());

        LeaveRequest updatedRequest = leaveRequestRepository.save(request);
        return EntityMapper.toLeaveRequestDTO(updatedRequest);
    }

    @Transactional
    public LeaveRequestDTO rejectLeaveRequest(Long requestId, Long approverId, String decisionComment) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStateException("Only PENDING leave requests can be rejected");
        }

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver user not found with id: " + approverId));

        request.setStatus(LeaveStatus.REJECTED);
        request.setApprover(approver);
        request.setDecisionComment(decisionComment);
        request.setDecisionDate(LocalDateTime.now());

        LeaveRequest updatedRequest = leaveRequestRepository.save(request);
        return EntityMapper.toLeaveRequestDTO(updatedRequest);
    }

    @Transactional
    public LeaveRequestDTO cancelLeaveRequest(Long requestId, Long userId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + requestId));

        if (!request.getUser().getId().equals(userId)) {
            throw new BusinessRuleException("You are not authorized to cancel this leave request");
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStateException("Only PENDING leave requests can be cancelled");
        }

        request.setStatus(LeaveStatus.CANCELLED);
        LeaveRequest updatedRequest = leaveRequestRepository.save(request);
        return EntityMapper.toLeaveRequestDTO(updatedRequest);
    }
}
