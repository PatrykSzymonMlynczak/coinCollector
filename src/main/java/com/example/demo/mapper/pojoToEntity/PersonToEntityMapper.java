package com.example.demo.mapper.pojoToEntity;

import com.example.demo.businessLogic.person.Person;
import com.example.demo.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonToEntityMapper {

    PersonEntity mapToEntity(Person person);

}
