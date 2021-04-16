package com.example.demo.GoogleApi;

import com.example.demo.Sale;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class GoogleDriveSaleFileManager {

    GoogleApiService googleApiService;

    public GoogleDriveSaleFileManager(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;

    }

    //todo handle exceptions
    public void updateSaleFile() {
        String lastId = getSaleFileId();
        try {
            googleApiService.getInstance().files().delete(lastId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        uploadSaleFile();
    }

    public void uploadSaleFile()  {
        File fileMetadata = new File();
        fileMetadata.setName("sale.json");
        java.io.File filePath = new java.io.File("sale.json");
        FileContent fileContent = new FileContent("application/json", filePath);

        File file = null;
        try {
            file = googleApiService.getInstance()
                    .files()
                    .create(fileMetadata,fileContent)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        saveNewSaleFileId(file.getId());
    }

    public List<Sale> getSalesList()  {
        String salesListString = getSaleStringFromGoogleDrive();
        if(salesListString.length() != 0) {
            Sale[] model = new Gson().fromJson(salesListString, Sale[].class);
            return Arrays.stream(model).collect(Collectors.toList());
        }else return new ArrayList<>();
    }

    private String getSaleStringFromGoogleDrive() {
        String fileId = getSaleFileId();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            if(!googleApiService.getInstance().files().get(fileId).isEmpty()){
                googleApiService.getInstance().files().get(fileId).executeMediaAndDownloadTo(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace(); //todo handle
        }

        return outputStream.toString();
    }

    private void saveNewSaleFileId(String fileId) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("googleDriveId/saleFileId", false);
            fileWriter.write(fileId);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSaleFileId() {
        String lastSaleFileId="";
        try {
            java.io.File myObj = new java.io.File("googleDriveId/saleFileId");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                lastSaleFileId = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lastSaleFileId;
    }

}
