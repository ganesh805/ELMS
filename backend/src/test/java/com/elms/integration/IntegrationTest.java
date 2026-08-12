package com.elms.integration;

import com.elms.dto.request.DecisionRequestDTO;
import com.elms.dto.request.LeaveCreateDTO;
import com.elms.dto.request.LoginRequestDTO;
import com.elms.dto.response.JwtResponseDTO;
import com.elms.dto.response.LeaveBalanceDTO;
import com.elms.dto.response.LeaveRequestDTO;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.entity.enums.Role;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveRequestRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User employee;
    private User manager;

    @BeforeEach
    public void setupTestUsers() {
        manager = userRepository.findByEmail("test.manager@elms.com")
                .orElseGet(() -> userRepository.save(User.builder()
                        .fullName("Test Manager")
                        .email("test.manager@elms.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(Role.MANAGER)
                        .department("Engineering")
                        .build()));

        employee = userRepository.findByEmail("test.employee@elms.com")
                .orElseGet(() -> userRepository.save(User.builder()
                        .fullName("Test Employee")
                        .email("test.employee@elms.com")
                        .password(passwordEncoder.encode("password123"))
                        .role(Role.EMPLOYEE)
                        .department("Engineering")
                        .manager(manager)
                        .build()));

        leaveRequestRepository.deleteAll();

        LeaveType leaveType = leaveTypeRepository.findAll().stream().findFirst().orElseGet(() -> 
                leaveTypeRepository.save(LeaveType.builder()
                        .name("Integration Annual Leave")
                        .defaultAnnualQuota(20)
                        .active(true)
                        .requiresApproval(true)
                        .build())
        );

        leaveBalanceRepository.deleteAll();
        leaveBalanceRepository.save(LeaveBalance.builder()
                .user(employee)
                .leaveType(leaveType)
                .year(2026)
                .allocated(20)
                .used(0)
                .remaining(20)
                .build());
    }

    @Test
    @DisplayName("End-to-End Flow: Login, Apply Leave, Validate Overlap, Manager Approve & Check Balance")
    public void testFullLeaveWorkflow() throws Exception {
        // 1. Login as Employee
        LoginRequestDTO empLogin = new LoginRequestDTO();
        empLogin.setEmail("test.employee@elms.com");
        empLogin.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empLogin)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponseDTO empAuth = objectMapper.readValue(loginResult.getResponse().getContentAsString(), JwtResponseDTO.class);
        assertNotNull(empAuth.getToken());

        // 2. Fetch Initial Leave Balances
        MvcResult balanceResult = mockMvc.perform(get("/api/leave-balances/my")
                .header("Authorization", "Bearer " + empAuth.getToken())
                .header("X-User-Id", empAuth.getUser().getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        LeaveBalanceDTO[] balances = objectMapper.readValue(balanceResult.getResponse().getContentAsString(), LeaveBalanceDTO[].class);
        assertTrue(balances.length > 0);
        int initialRemaining = balances[0].getRemaining();

        // 3. Submit Leave Request
        LocalDate startDate = LocalDate.now().plusDays(14);
        LocalDate endDate = startDate.plusDays(2);

        LeaveCreateDTO leaveRequest = new LeaveCreateDTO();
        leaveRequest.setLeaveTypeId(balances[0].getLeaveTypeId());
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);
        leaveRequest.setReason("Integration Test Annual Vacation");

        MvcResult createResult = mockMvc.perform(post("/api/leaves")
                .header("Authorization", "Bearer " + empAuth.getToken())
                .header("X-User-Id", empAuth.getUser().getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaveRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        LeaveRequestDTO createdLeave = objectMapper.readValue(createResult.getResponse().getContentAsString(), LeaveRequestDTO.class);
        assertEquals(com.elms.entity.enums.LeaveStatus.PENDING, createdLeave.getStatus());

        // 4. Verify Overlap Error (Rule 2)
        mockMvc.perform(post("/api/leaves")
                .header("Authorization", "Bearer " + empAuth.getToken())
                .header("X-User-Id", empAuth.getUser().getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leaveRequest)))
                .andExpect(status().isBadRequest());

        // 5. Login as Manager
        LoginRequestDTO mgrLogin = new LoginRequestDTO();
        mgrLogin.setEmail("test.manager@elms.com");
        mgrLogin.setPassword("password123");

        MvcResult mgrLoginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mgrLogin)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponseDTO mgrAuth = objectMapper.readValue(mgrLoginResult.getResponse().getContentAsString(), JwtResponseDTO.class);

        // 6. Manager Approves Leave Request
        DecisionRequestDTO decision = new DecisionRequestDTO();
        decision.setDecisionComment("Approved in E2E integration test");

        mockMvc.perform(put("/api/leaves/" + createdLeave.getId() + "/approve")
                .header("Authorization", "Bearer " + mgrAuth.getToken())
                .header("X-User-Id", mgrAuth.getUser().getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(decision)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        // 7. Verify Automatic Balance Deduction
        MvcResult updatedBalanceResult = mockMvc.perform(get("/api/leave-balances/my")
                .header("Authorization", "Bearer " + empAuth.getToken())
                .header("X-User-Id", empAuth.getUser().getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        LeaveBalanceDTO[] updatedBalances = objectMapper.readValue(updatedBalanceResult.getResponse().getContentAsString(), LeaveBalanceDTO[].class);
        assertEquals(initialRemaining - createdLeave.getNumberOfDays(), updatedBalances[0].getRemaining());
    }
}
