package com.example.demo.mapper.pojoToDto;

import com.example.demo.dto.ProductDto;
import com.example.demo.businessLogic.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductToDtoMapper {

    ProductDto mapToDto(Product product);
}
