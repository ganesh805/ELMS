package com.elms.controller;

import com.elms.dto.response.LeaveTypeDTO;
import com.elms.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @GetMapping
    public ResponseEntity<List<LeaveTypeDTO>> getActiveLeaveTypes() {
        return ResponseEntity.ok(leaveTypeService.getAllActiveLeaveTypes());
    }
}
