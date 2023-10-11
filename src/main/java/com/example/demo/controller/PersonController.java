package com.example.demo.controller;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.dto.PersonDto;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.postgres.service.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    PersonService personService;

    private final PersonMapper personMapper;
    @Autowired
    public PersonController(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Operation(summary  = "Endpoint allowing to add new Person")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Successfully added new Person"),
            @ApiResponse(responseCode  = "400", description = "Bad request")})
    @PostMapping("/{personName}")
    public PersonDto savePerson(@Parameter(description = "Person Name", example = "Zamor")
                                    @PathVariable String personName){
        PersonDto person = new PersonDto(personName);
        personService.savePerson(personMapper.dtoToPerson(person));
        return person;
    }

    @Operation(summary  = "Endpoint updating debt, not changing total earnings")
    @PutMapping("/updateDebt")
    public PersonDto updateDebt(Float debt, String name){
        //todo ignore case
        personService.updateDebt(debt,name);
        //todo handle total money -> make req to saleService with position "debt payed"
        return personMapper.personToDto(personService.getPerson(name));
    }

    @PutMapping("/payDebt")
    public PersonDto payDebt(Float debt, String name){
        personService.payDebt(debt,name);
        //todo handle total money -> make req to saleService with position "debt payed"
        return personMapper.personToDto(personService.getPerson(name));
    }

    @GetMapping("/allDebt")
    public List<PersonDto> getDebts(){
        return personService.getAllPerson().stream()
                .filter(person -> person.getDebt() != 0)
                .map(personMapper::personToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/collectDebt")
    public Double collectDebt(){
        return personService.getAllPerson().stream()
                .filter(person -> person.getDebt() != 0).mapToDouble(Person::getDebt).sum();
    }

    @Operation(summary  = "Endpoint allowing get all Persons")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "Successfully received all Persons"),
            @ApiResponse(responseCode  = "400", description = "Bad request")})
    @GetMapping
    public List<PersonDto> getAllPersons(){
        return personService.getAllPerson()
                .stream()
                .map(personMapper::personToDto)
                .collect(Collectors.toList());
    }
}