package com.example.demo.mapper.pojoToEntity;

import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaleToEntityMapper {

    SaleEntity mapToEntity(Sale sale);
}
