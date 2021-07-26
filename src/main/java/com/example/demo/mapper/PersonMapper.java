package com.example.demo.mapper;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.dto.PersonDto;
import com.example.demo.postgres.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {

    PersonEntity personToEntity(Person person);
    Person entityToPerson(PersonEntity personEntity);
    PersonDto personToDto(Person person);
    Person dtoToPerson(PersonDto personDto);

}
