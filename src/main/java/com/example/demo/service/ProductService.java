package com.example.demo.service;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.ProductRepo;
import com.example.demo.mapper.pojoToEntity.ProductToEntityMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
//@Primary
@Qualifier("postgres")
public class ProductService implements ProductRepo {

    ProductRepoPostgres productRepoPostgres;
    ProductToEntityMapper productToEntityMapper;

    public ProductService(ProductRepoPostgres productRepoPostgres, ProductToEntityMapper productToEntityMapper) {
        this.productRepoPostgres = productRepoPostgres;
        this.productToEntityMapper = productToEntityMapper;
    }

    @Override
    public Product saveProduct(Product product) {
        productRepoPostgres.save(productToEntityMapper.mapToEntity(product));
        return product;
    }

    @Override
    public Product getSortPricingByProductAndMyPrice(String product, Float myPrice) {
        return null;
    }

    @Override
    public void deleteProduct(String product, Float myPrice) {

    }
}
