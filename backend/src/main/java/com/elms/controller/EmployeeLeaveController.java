package com.elms.controller;

import com.elms.dto.request.LeaveCreateDTO;
import com.elms.dto.response.LeaveRequestDTO;
import com.elms.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeLeaveController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequestDTO> createLeaveRequest(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody LeaveCreateDTO dto) {
        LeaveRequestDTO createdRequest = leaveRequestService.createLeaveRequest(userId, dto, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @GetMapping("/my")
    public ResponseEntity<List<LeaveRequestDTO>> getMyLeaveRequests(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(leaveRequestService.getMyLeaveRequests(userId));
    }
}
