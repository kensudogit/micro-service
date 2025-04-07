package com.example.contractmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractController {

    @GetMapping("/contracts")
    public String getContracts() {
        return "Contract data";
    }
}