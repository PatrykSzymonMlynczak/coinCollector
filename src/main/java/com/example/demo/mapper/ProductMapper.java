package com.example.demo.mapper;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.dto.ProductDto;
import com.example.demo.postgres.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductEntity productToEntity(Product product);
    Product entityToProduct(ProductEntity productEntity);
    ProductDto productToDto(Product product);
}
