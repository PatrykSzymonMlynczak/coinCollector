package com.example.demo.GoogleApi;

import com.example.demo.Sale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoogleBackupController {

    private final GoogleDriveSaleFileManager fileManager;

    public GoogleBackupController(GoogleDriveSaleFileManager fileManager) {
        this.fileManager = fileManager;
    }

    @GetMapping({"/ss"})
    public List<Sale> getSale(){
        return fileManager.getSalesList();
    }

    @PostMapping("/u")
    public void uploadSale(){
        fileManager.uploadSaleFile();
    }

    @GetMapping("/u")
    public void updateSale(){
        fileManager.updateSaleFile();
    }
}
