package com.elms.controller;

import com.elms.dto.request.LeaveCreateDTO;
import com.elms.dto.response.LeaveRequestDTO;
import com.elms.entity.enums.LeaveStatus;
import com.elms.service.AttachmentService;
import com.elms.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmployeeLeaveController {

    private final LeaveRequestService leaveRequestService;
    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<LeaveRequestDTO> createLeaveRequest(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody LeaveCreateDTO dto,
            @RequestParam(name = "attachmentFileName", required = false) String attachmentFileName) {
        LeaveRequestDTO createdRequest = leaveRequestService.createLeaveRequest(userId, dto, attachmentFileName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @GetMapping("/my")
    public ResponseEntity<List<LeaveRequestDTO>> getMyLeaveRequests(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(name = "status", required = false) LeaveStatus status) {
        return ResponseEntity.ok(leaveRequestService.getMyLeaveRequests(userId, status));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequestDTO> cancelLeaveRequest(
            @PathVariable("id") Long requestId,
            @RequestHeader("X-User-Id") Long userId) {
        LeaveRequestDTO cancelledRequest = leaveRequestService.cancelLeaveRequest(requestId, userId);
        return ResponseEntity.ok(cancelledRequest);
    }

    @PostMapping("/upload-attachment")
    public ResponseEntity<Map<String, String>> uploadAttachment(@RequestParam("file") MultipartFile file) {
        String storedFileName = attachmentService.storeFile(file);
        return ResponseEntity.ok(Map.of("fileName", storedFileName));
    }

    @GetMapping("/attachments/{fileName:.+}")
    @io.swagger.v3.oas.annotations.Operation(
        summary = "Download leave application attachment file",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "File stream download",
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
        }
    )
    public ResponseEntity<Resource> downloadAttachment(@PathVariable("fileName") String fileName) {
        Resource resource = attachmentService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
