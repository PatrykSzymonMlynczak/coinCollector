package com.example.demo.GoogleApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleBackupController {

    private final GoogleDriveProductFileManager googleDriveProductFileManager;
    private final GoogleDriveSaleFileManager googleDriveSaleFileManager;

    public GoogleBackupController(GoogleDriveProductFileManager googleDriveProductFileManager,
                                  GoogleDriveSaleFileManager googleDriveSaleFileManager) {
        this.googleDriveProductFileManager = googleDriveProductFileManager;
        this.googleDriveSaleFileManager = googleDriveSaleFileManager;
    }

    @GetMapping("/pu")
    public void productsSend(){
        googleDriveProductFileManager.uploadGoogleFile(GoogleFile.PRODUCT);
    }

/*
    @GetMapping({"/ss"})
    public List<Sale> getSale(){
        return (List<Sale>) googleDriveFileManager.getGoogleFileList(GoogleFile.SALE);
    }*/

    @PostMapping("/u")
    public void uploadSale(){
        googleDriveProductFileManager.uploadGoogleFile(GoogleFile.SALE);
    }

    @GetMapping("/u")
    public void updateSale(){
        googleDriveProductFileManager.updateGoogleFile(GoogleFile.SALE);
    }
}
