package com.elms.controller;

import com.elms.dto.request.*;
import com.elms.dto.response.*;
import com.elms.entity.enums.LeaveStatus;
import com.elms.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminUserService adminUserService;
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveRequestService leaveRequestService;
    private final LeaveTypeService leaveTypeService;
    private final HolidayService holidayService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO created = adminUserService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable("id") Long userId,
            @RequestBody UserUpdateDTO dto) {
        UserDTO updated = adminUserService.updateUser(userId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leave-types")
    public ResponseEntity<LeaveTypeDTO> createLeaveType(@Valid @RequestBody LeaveTypeCreateDTO dto) {
        LeaveTypeDTO created = leaveTypeService.createLeaveType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/leave-types/{id}")
    public ResponseEntity<LeaveTypeDTO> updateLeaveType(
            @PathVariable("id") Long id,
            @RequestBody LeaveTypeUpdateDTO dto) {
        LeaveTypeDTO updated = leaveTypeService.updateLeaveType(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/leave-types/{id}")
    public ResponseEntity<Void> deleteLeaveType(@PathVariable("id") Long id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/holidays")
    public ResponseEntity<HolidayDTO> createHoliday(@Valid @RequestBody HolidayCreateDTO dto) {
        HolidayDTO created = holidayService.createHoliday(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable("id") Long id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leave-balances/adjust")
    public ResponseEntity<LeaveBalanceDTO> adjustBalance(@Valid @RequestBody BalanceAdjustDTO dto) {
        LeaveBalanceDTO adjusted = leaveBalanceService.adjustBalance(dto);
        return ResponseEntity.ok(adjusted);
    }

    @GetMapping("/leaves")
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaves(
            @RequestParam(name = "status", required = false) LeaveStatus status) {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests(status));
    }

    @PutMapping("/leaves/{id}/revoke")
    public ResponseEntity<LeaveRequestDTO> revokeApprovedLeave(
            @PathVariable("id") Long requestId,
            @RequestHeader("X-User-Id") Long adminId,
            @RequestBody(required = false) DecisionRequestDTO dto) {
        String reason = dto != null ? dto.getDecisionComment() : null;
        LeaveRequestDTO revoked = leaveRequestService.revokeApprovedLeaveRequest(requestId, adminId, reason);
        return ResponseEntity.ok(revoked);
    }
}
