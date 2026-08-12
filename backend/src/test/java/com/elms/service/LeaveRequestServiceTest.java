package com.elms.service;

import com.elms.dto.request.LeaveCreateDTO;
import com.elms.dto.response.LeaveRequestDTO;
import com.elms.entity.LeaveRequest;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.entity.enums.LeaveStatus;
import com.elms.exception.BusinessRuleException;
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
    private WorkingDayService workingDayService;

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    private User sampleUser;
    private LeaveType sampleLeaveType;
    private LeaveCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .build();

        sampleLeaveType = LeaveType.builder()
                .id(1L)
                .name("Annual Leave")
                .active(true)
                .build();

        createDTO = new LeaveCreateDTO();
        createDTO.setLeaveTypeId(1L);
        createDTO.setStartDate(LocalDate.now().plusDays(10));
        createDTO.setEndDate(LocalDate.now().plusDays(14));
        createDTO.setReason("Vacation rest");
    }

    @Test
    void testCreateLeaveRequest_NoOverlap_Succeeds() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(sampleLeaveType));
        when(leaveRequestRepository.findOverlappingRequests(eq(1L), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(workingDayService.calculateWorkingDays(any(), any())).thenReturn(5);
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
}
