package com.example.demo;


import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.dto.SaleDto;
import com.example.demo.fileManager.JsonFileManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.TreeMap;

//todo - exception advice ApiReturn Obj
//todo - proper exception http codes
//todo - product controller

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationE2eTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JsonFileManager jsonFileManager;

    @AfterEach
    public void clean(){
        jsonFileManager.clearAllSales();
        jsonFileManager.clearAllProducts();
    }

    @BeforeEach
    public void setupProduct() throws Exception {
        TreeMap<Float,Float> sortPricing = new TreeMap<>();
        sortPricing.put(1F,20F);
        jsonFileManager.saveNewProductToFileAsJson(new Product("test",sortPricing, 10F));
    }

    @Test
    public void should_return_sale_after_GET() throws Exception {
        //given
        mockMvc.perform(MockMvcRequestBuilders
                .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                    "test",      "10",      "1",        "Zamor",    null));
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/sale/all"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto[] sales = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto[].class);
        //then
        Assertions.assertEquals("Zamor",sales[0].getPerson().getName());
        Assertions.assertEquals(1F,sales[0].getQuantity());
    }

    @Test
    public void should_return_() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                    "test",      "10",      "1",        "Zamor",    null))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto sale = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto.class);
        Assertions.assertEquals("Zamor",sale.getPerson().getName());
        Assertions.assertEquals(1,sale.getQuantity());
        Assertions.assertEquals(10,sale.getProduct().getMyPrice());
    }

    @Test
    public void should_save_sale_to_file_E2E() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                        "test",      "10",      "1",        "Zamor",    null))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto newSale = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto.class);

        //then
        List<Sale> salesFromFile = jsonFileManager.readSaleListFromFile();
        Assertions.assertEquals(salesFromFile.get(0).getPerson().getName(),newSale.getPerson().getName());
        Assertions.assertEquals(salesFromFile.get(0).getTransactionDate(),newSale.getTransactionDate());
    }

    @Test
    public void should_throw_sort_not_exist_exception() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                    "test",      "1",      "1",        "Zamor",    null))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
        String actualMessage = mvcResult.getResolvedException().getMessage();

        //then
        Assertions.assertEquals("You can't add sale because Sort not exists: "+"test"+" price: "+"1.0",actualMessage);

    }






}
