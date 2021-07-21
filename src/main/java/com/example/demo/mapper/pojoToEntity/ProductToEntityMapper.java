package com.example.demo.mapper.pojoToEntity;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductToEntityMapper {

    ProductEntity mapToEntity(Product product);
}
