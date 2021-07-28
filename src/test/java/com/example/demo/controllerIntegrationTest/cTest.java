package com.example.demo.controllerIntegrationTest;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.postgres.entity.PersonEntity;
import com.example.demo.postgres.repository.PersonRepoPostgres;
import com.example.demo.postgres.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(PersonController.class)
//@ActiveProfiles("dev")
public class cTest {

    private MockMvc mockMvc;

    @Mock
    private PersonRepoPostgres personRepoPostgres;
    @Mock
    private PersonMapper personMapper;
    @InjectMocks
    private PersonService personService;

    @Test
    void test(){
        Mockito.when(personMapper.entityToPerson(any(PersonEntity.class))).thenReturn(new Person("Zamor"));
        Mockito.when(personRepoPostgres.findAll()).thenReturn(List.of(new PersonEntity()));
        List<Person> personArrayList = personService.getAllPerson();
        //Unable to use @WebMvcTest, Cause:
        /*Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException:
        Error creating bean with name 'personController' defined in file
        [C:\Users\Patryk\Desktop\c_dev\coinCollector-withoutGDrive\build\classes\java\main\com\example\demo\controller\PersonController.class]:
        Unsatisfied dependency expressed through constructor parameter 0;
        nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException:
        No qualifying bean of type 'com.example.demo.mapper.PersonMapper'
        available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}*/
        Assertions.assertEquals(personArrayList.get(0).getName(), "Zamor");
    }

}
