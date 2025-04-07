package com.example.approvalrequest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApprovalController {

    @GetMapping("/approvals")
    public String getApprovals() {
        return "Approval request data";
    }
}