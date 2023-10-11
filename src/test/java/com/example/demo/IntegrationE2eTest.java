package com.example.demo;

import com.example.demo.dto.SaleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.TreeMap;


/**
 *
 * In case of standard Integration tests
 * FileManager should be mocked and tested separately
 * but I decided to simplify that process and do it at once
 * until I decide to adapt a real database
 */

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationE2eTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeEach
    public void setupProduct() throws Exception {
        TreeMap<Float, Float> map = new TreeMap<>();
        map.put(1F, 5F);
        String json = objectMapper.writeValueAsString(map);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/product/{productName}/{myPrice}", "test", "10")
                .contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    public void should_return_sale_after_sale_GET() throws Exception {
        //given
        mockMvc.perform(MockMvcRequestBuilders
                .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                        "test", "10", "1", "Zamor", null));
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/sale/all"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto[] sales = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto[].class);

        //then
        Assertions.assertEquals("Zamor", sales[0].getPerson().getName());
        Assertions.assertEquals(1F, sales[0].getQuantity());
    }

    @Test
    public void should_return_proper_values_after_sale_POST() throws Exception {

        //when

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/person/{personName}",
                                "Zamor"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                "test", "10", "1", "Zamor", null))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        //then
        SaleDto sale = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto.class);
        Assertions.assertEquals("Zamor", sale.getPerson().getName());
        Assertions.assertEquals(1, sale.getQuantity());
        Assertions.assertEquals(10, sale.getProduct().getMyPrice());
    }

/*    @Test
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
    }*/

    @Test
    public void should_throw_sort_not_exist_exception() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}",
                                "test", "1", "1", "Zamor", null))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
        String actualMessage = mvcResult.getResolvedException().getMessage();

        //then
        Assertions.assertEquals("You can't add sale because Sort not exists: " + "test" + " price: " + "1.0", actualMessage);

    }

    @Test
    public void should_throw_sort_already_exists_exception() throws Exception {
        //given
        TreeMap<Float, Float> map = new TreeMap<>();
        map.put(1F, 5F);
        String json = objectMapper.writeValueAsString(map);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/product/{productName}/{myPrice}", "test", "10")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CONFLICT.value()))
                .andReturn();
        String actualMessage = mvcResult.getResolvedException().getMessage();

        //then
        Assertions.assertEquals("You can't add sort pricing because it already exists: " + "test" + " price: " + "10.0",
                actualMessage);

    }
}