package com.example.demo.GoogleApi;

import com.example.demo.Product;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleDriveProductFileManager extends GoogleDriveFileManagerAbstract {

    public GoogleDriveProductFileManager(GoogleApiService googleApiService) {
        super(googleApiService);
    }

    public List<Product> getGoogleProductFileList()  {
        String salesListString = getFileFromGoogleDrive(GoogleFile.PRODUCT);
        if(salesListString.length() != 0) {
            Product[] model = new Gson().fromJson(salesListString, Product[].class);
            return Arrays.stream(model).collect(Collectors.toList());
        }else return new ArrayList<>();
    }


}
