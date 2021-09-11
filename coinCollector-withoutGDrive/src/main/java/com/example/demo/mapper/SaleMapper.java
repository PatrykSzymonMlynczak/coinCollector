package com.example.demo.mapper;

import com.example.demo.dto.SaleDto;
import com.example.demo.businessLogic.sale.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleMapper {

    SaleDto mapToDto(Sale sale);
}
