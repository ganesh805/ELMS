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
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveRequestRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveRequestServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private User sampleUser;
    private User sampleApprover;
    private LeaveType sampleLeaveType;
    private LeaveBalance sampleBalance;
    private LeaveCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .build();

        sampleApprover = User.builder()
                .id(2L)
                .fullName("Sarah Jenkins")
                .email("sarah@example.com")
                .build();

        sampleLeaveType = LeaveType.builder()
                .id(1L)
                .name("Annual Leave")
                .active(true)
                .build();

        sampleBalance = LeaveBalance.builder()
                .id(10L)
                .user(sampleUser)
                .leaveType(sampleLeaveType)
                .year(2026)
                .allocated(18)
                .used(3)
                .remaining(15)
                .build();

        createDTO = new LeaveCreateDTO();
        createDTO.setLeaveTypeId(1L);
        createDTO.setStartDate(LocalDate.of(2026, 10, 5));
        createDTO.setEndDate(LocalDate.of(2026, 10, 9));
        createDTO.setReason("Vacation rest");
    }

    @Test
    void testCreateLeaveRequest_SufficientBalance_Succeeds() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveType));
        when(leaveRequestRepository.findOverlappingRequests(eq(1L), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(workingDayService.calculateWorkingDays(any(), any())).thenReturn(5);
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeIdAndYear(eq(1L), eq(1L), anyInt()))
                .thenReturn(Optional.of(sampleBalance));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(i -> {
            LeaveRequest req = i.getArgument(0);
            req.setId(100L);
            return req;
        });

        LeaveRequestDTO response = leaveRequestService.createLeaveRequest(1L, createDTO, null);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(LeaveStatus.PENDING, response.getStatus());
        assertEquals(5, response.getNumberOfDays());
    }

    @Test
    void testCreateLeaveRequest_InsufficientBalance_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveType));
        when(leaveRequestRepository.findOverlappingRequests(eq(1L), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(workingDayService.calculateWorkingDays(any(), any())).thenReturn(20);

        sampleBalance.setRemaining(5);
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeIdAndYear(eq(1L), eq(1L), anyInt()))
                .thenReturn(Optional.of(sampleBalance));

        InsufficientLeaveBalanceException ex = assertThrows(InsufficientLeaveBalanceException.class,
                () -> leaveRequestService.createLeaveRequest(1L, createDTO, null));

        assertTrue(ex.getMessage().contains("exceeds remaining balance"));
    }

    @Test
    void testCreateLeaveRequest_OverlapExists_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveType));

        LeaveRequest existingRequest = LeaveRequest.builder()
                .id(50L)
                .status(LeaveStatus.PENDING)
                .build();

        when(leaveRequestRepository.findOverlappingRequests(eq(1L), any(), any(), any()))
                .thenReturn(List.of(existingRequest));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> leaveRequestService.createLeaveRequest(1L, createDTO, null));

        assertTrue(ex.getMessage().contains("overlap"));
    }

    @Test
    void testApproveLeaveRequest_DeductsBalanceAndApproves() {
        LeaveRequest pendingRequest = LeaveRequest.builder()
                .id(10L)
                .user(sampleUser)
                .leaveType(sampleLeaveType)
                .startDate(LocalDate.of(2026, 10, 5))
                .endDate(LocalDate.of(2026, 10, 9))
                .numberOfDays(5)
                .status(LeaveStatus.PENDING)
                .build();

        when(leaveRequestRepository.findById(10L)).thenReturn(Optional.of(pendingRequest));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sampleApprover));
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeIdAndYear(1L, 1L, 2026))
                .thenReturn(Optional.of(sampleBalance));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(i -> i.getArgument(0));

        LeaveRequestDTO result = leaveRequestService.approveLeaveRequest(10L, 2L, "Approved!");

        assertEquals(LeaveStatus.APPROVED, result.getStatus());
        assertEquals(8, sampleBalance.getUsed());
        assertEquals(10, sampleBalance.getRemaining());
        verify(leaveBalanceRepository).save(sampleBalance);
    }

    @Test
    void testRevokeApprovedLeaveRequest_RecreditsBalanceAndCancels() {
        LeaveRequest approvedRequest = LeaveRequest.builder()
                .id(10L)
                .user(sampleUser)
                .leaveType(sampleLeaveType)
                .startDate(LocalDate.of(2026, 10, 5))
                .endDate(LocalDate.of(2026, 10, 9))
                .numberOfDays(5)
                .status(LeaveStatus.APPROVED)
                .build();

        sampleBalance.setUsed(8);
        sampleBalance.setRemaining(10);

        when(leaveRequestRepository.findById(10L)).thenReturn(Optional.of(approvedRequest));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sampleApprover));
        when(leaveBalanceRepository.findByUserIdAndLeaveTypeIdAndYear(1L, 1L, 2026))
                .thenReturn(Optional.of(sampleBalance));
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(i -> i.getArgument(0));

        LeaveRequestDTO result = leaveRequestService.revokeApprovedLeaveRequest(10L, 2L, "Administrative revocation");

        assertEquals(LeaveStatus.CANCELLED, result.getStatus());
        assertEquals(3, sampleBalance.getUsed());
        assertEquals(15, sampleBalance.getRemaining());
        verify(leaveBalanceRepository).save(sampleBalance);
    }
}
