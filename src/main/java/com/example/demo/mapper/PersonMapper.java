package com.example.demo.mapper;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.dto.PersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper {

    PersonDto mapToDto(Person person);
}
