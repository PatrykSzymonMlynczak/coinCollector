package com.example.demo.businessLogic.googleApi;

import com.example.demo.businessLogic.sale.Sale;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleDriveSaleFileManager extends GoogleDriveFileManagerAbstract {

    public GoogleDriveSaleFileManager(GoogleApiService googleApiService) {
        super(googleApiService);
    }

    public List<Sale> getGoogleSaleFileList()  {
        String salesListString = getFileFromGoogleDrive(GoogleFile.SALE);
        if(salesListString.length() != 0) {
            Sale[] model = new Gson().fromJson(salesListString, Sale[].class);
            return Arrays.stream(model).collect(Collectors.toList());
        }else return new ArrayList<>();
    }


}
