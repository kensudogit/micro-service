package com.example.dailyreportmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DailyReportController {

    @GetMapping("/reports")
    public String getReports() {
        return "Daily report data";
    }
}