package com.example.demo.mapper;

import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.dto.SaleDto;
import com.example.demo.entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleMapper {

    SaleEntity saleToEntity(Sale sale);
    SaleDto saleToDto(Sale sale);
}
