package com.example.assetmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssetController {

    @GetMapping("/assets")
    public String getAssets() {
        return "Asset data";
    }
}