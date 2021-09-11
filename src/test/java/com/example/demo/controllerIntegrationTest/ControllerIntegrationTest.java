package com.example.demo.controllerIntegrationTest;

import com.example.demo.businessLogic.person.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-postgresTest.properties")
@Sql(scripts = "classpath:populateDb/insertData.sql")
public class ControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldReturnAllPersons() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/person"))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        Person[] persons = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Person[].class);
        Assertions.assertEquals(2, persons.length);
    }

}
