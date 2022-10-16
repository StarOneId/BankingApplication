package com.bank.notification.controller;

import com.bank.notification.service.NotificationService;
import com.bank.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        notificationService.sendEmail(to, subject, body);
        return ResponseEntity.ok(ApiResponse.success(null, "Email sent successfully"));
    }
}
