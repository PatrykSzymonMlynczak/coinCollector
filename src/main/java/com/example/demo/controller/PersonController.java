package com.example.demo.controller;

import com.example.demo.dto.PersonDto;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.repositoryContract.PersonRepo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    @Qualifier("${data.service}")
    PersonRepo personRepo;

    private final PersonMapper personMapper;
    @Autowired
    public PersonController(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @ApiOperation(value = "Endpoint allowing to add new Person")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new Person"),
            @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping("/{personName}")
    public PersonDto savePerson(@ApiParam(value = "Person Name", example = "Zamor")
                                    @PathVariable String personName){
        PersonDto person = new PersonDto(personName);
        personRepo.savePerson(personMapper.dtoToPerson(person));
        return person;
    }//todo handle adding same person more than once

    @ApiOperation(value = "Endpoint allowing get all Persons")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully received all Persons"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping
    public List<PersonDto> getAllPersons(){
        return personRepo.getAllPerson()
                .stream()
                .map(personMapper::personToDto)
                .collect(Collectors.toList());
    }
}
