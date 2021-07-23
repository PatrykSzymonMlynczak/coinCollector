package com.example.demo.postgres.service;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.entity.ProductEntity;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import com.example.demo.repositoryContract.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("postgres")
@AllArgsConstructor
public class ProductService implements ProductRepo {

    private final ProductRepoPostgres productRepoPostgres;
    private final ProductMapper productMapper;

    @Override
    public Product saveProduct(Product product) {
        if(!productRepoPostgres.existsByNameAndMyPrice(product.getName(),product.getMyPrice())){
            ProductEntity productEntity = productMapper.productToEntity(product);
            productRepoPostgres.save(productEntity);
            return product;
        }else throw new ProductAlreadyExistsException(product.getName(),product.getMyPrice());
    }

    @Override
    public Product getProductByNameAndMyPrice(String productName, Float myPrice) {
        if(productRepoPostgres.existsByNameAndMyPrice(productName,myPrice)){
            ProductEntity productEntity = productRepoPostgres.getByNameAndMyPrice(productName,myPrice);
            return productMapper.entityToProduct(productEntity);
        }else throw new ProductNotExistException(productName, myPrice);
    }

    @Override
    public void deleteProduct(String product, Float myPrice) {
        productRepoPostgres.deleteByNameAndMyPrice(product, myPrice);
    }
}