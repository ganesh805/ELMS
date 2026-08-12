package com.elms.service;

import com.elms.dto.response.LeaveRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Async
    public void sendLeaveSubmissionNotification(LeaveRequestDTO request) {
        String subject = "New Leave Application Submitted - " + request.getUserName();
        String text = String.format("Hello,\n\nEmployee %s has submitted a leave request for %d day(s) from %s to %s.\nReason: %s\n\nPlease log in to review.",
                request.getUserName(), request.getNumberOfDays(), request.getStartDate(), request.getEndDate(), request.getReason());
        
        sendEmail("manager@elms.com", subject, text);
    }

    @Async
    public void sendApprovalNotification(LeaveRequestDTO request) {
        String subject = "Leave Application Approved";
        String text = String.format("Hello %s,\n\nYour leave application from %s to %s (%d day(s)) has been APPROVED by %s.\nDecision Comment: %s\n\nBest regards,\nELMS Team",
                request.getUserName(), request.getStartDate(), request.getEndDate(), request.getNumberOfDays(),
                request.getApproverName() != null ? request.getApproverName() : "Manager",
                request.getDecisionComment() != null ? request.getDecisionComment() : "None");

        sendEmail("employee@elms.com", subject, text);
    }

    @Async
    public void sendRejectionNotification(LeaveRequestDTO request) {
        String subject = "Leave Application Rejected";
        String text = String.format("Hello %s,\n\nYour leave application from %s to %s (%d day(s)) has been REJECTED by %s.\nReason/Comment: %s\n\nBest regards,\nELMS Team",
                request.getUserName(), request.getStartDate(), request.getEndDate(), request.getNumberOfDays(),
                request.getApproverName() != null ? request.getApproverName() : "Manager",
                request.getDecisionComment() != null ? request.getDecisionComment() : "None");

        sendEmail("employee@elms.com", subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        log.info("[EMAIL NOTIFICATION] To: {} | Subject: {}\nBody:\n{}", to, subject, text);
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(text);
                mailSender.send(message);
            } catch (Exception ex) {
                log.warn("Could not send SMTP email notification: {}", ex.getMessage());
            }
        }
    }
}
