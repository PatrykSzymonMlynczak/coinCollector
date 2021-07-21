package com.example.demo.mapper.pojoToDto;

import com.example.demo.dto.SaleDto;
import com.example.demo.businessLogic.sale.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleToDtoMapper {

    SaleDto mapToDto(Sale sale);
}
