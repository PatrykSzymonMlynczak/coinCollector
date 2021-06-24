package com.example.demo;


import com.example.demo.dto.SaleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class integration {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void should() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/sale/all"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        SaleDto[] sales = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SaleDto[].class);
    }




}
