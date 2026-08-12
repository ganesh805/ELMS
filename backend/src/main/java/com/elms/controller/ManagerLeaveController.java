package com.elms.controller;

import com.elms.dto.request.DecisionRequestDTO;
import com.elms.dto.response.LeaveRequestDTO;
import com.elms.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ManagerLeaveController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequestDTO>> getPendingApprovals(@RequestHeader("X-User-Id") Long managerId) {
        return ResponseEntity.ok(leaveRequestService.getPendingApprovalsForManager(managerId));
    }

    @GetMapping("/team")
    public ResponseEntity<List<LeaveRequestDTO>> getTeamLeaveRequests(@RequestHeader("X-User-Id") Long managerId) {
        return ResponseEntity.ok(leaveRequestService.getTeamLeaveRequestsForManager(managerId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestDTO> approveLeaveRequest(
            @PathVariable("id") Long requestId,
            @RequestHeader("X-User-Id") Long approverId,
            @RequestBody(required = false) DecisionRequestDTO dto) {
        String comment = dto != null ? dto.getDecisionComment() : null;
        LeaveRequestDTO approved = leaveRequestService.approveLeaveRequest(requestId, approverId, comment);
        return ResponseEntity.ok(approved);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestDTO> rejectLeaveRequest(
            @PathVariable("id") Long requestId,
            @RequestHeader("X-User-Id") Long approverId,
            @RequestBody(required = false) DecisionRequestDTO dto) {
        String comment = dto != null ? dto.getDecisionComment() : null;
        LeaveRequestDTO rejected = leaveRequestService.rejectLeaveRequest(requestId, approverId, comment);
        return ResponseEntity.ok(rejected);
    }
}
