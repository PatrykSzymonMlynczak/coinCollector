package com.example.demo.googleApi;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public abstract class GoogleDriveFileManagerAbstract {

    GoogleApiService googleApiService;

    public GoogleDriveFileManagerAbstract(GoogleApiService googleApiService) {
        this.googleApiService = googleApiService;
    }

    public void uploadGoogleFile(GoogleFile googleFile)  {
        File fileMetadata = new File();
        fileMetadata.setName(googleFile.type+".json");
        java.io.File filePath = new java.io.File(googleFile.type+".json");
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

        saveNewFileId(file.getId(), googleFile);
    }

    //todo handle exceptions
    public void updateGoogleFile(GoogleFile googleFile) {
        String lastId = getFileId(googleFile);
        try {
            googleApiService.getInstance().files().delete(lastId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        uploadGoogleFile(googleFile);
    }

    public String getFileFromGoogleDrive(GoogleFile googleFile) {
        String fileId = getFileId(googleFile);
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

    protected void saveNewFileId(String fileId, GoogleFile googleFile) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("googleDriveId/"+ googleFile.type+"FileId", false);
            fileWriter.write(fileId);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getFileId(GoogleFile googleFile) {
        String lastSaleFileId="";
        try {
            java.io.File myObj = new java.io.File("googleDriveId/"+ googleFile.type+"FileId");
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
