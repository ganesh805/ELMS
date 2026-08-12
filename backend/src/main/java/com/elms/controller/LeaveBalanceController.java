package com.elms.controller;

import com.elms.dto.response.LeaveBalanceDTO;
import com.elms.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    @GetMapping("/my")
    public ResponseEntity<List<LeaveBalanceDTO>> getMyLeaveBalances(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(name = "year", required = false) Integer year) {
        return ResponseEntity.ok(leaveBalanceService.getUserLeaveBalances(userId, year));
    }
}
