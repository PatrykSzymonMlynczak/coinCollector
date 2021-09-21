package com.example.demo.postgres.service;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.exception.NotEnoughSortException;
import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.postgres.entity.ProductEntity;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import com.example.demo.repositoryContract.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService implements ProductRepo {

    private final ProductRepoPostgres productRepoPostgres;
    private final ProductMapper productMapper;

    @Override
    public Product saveProduct(Product product) {
        if (!productRepoPostgres.existsByNameIgnoreCase(product.getName())) {
            ProductEntity productEntity = productMapper.productToEntity(product);
            productRepoPostgres.save(productEntity);
            return product;
        } else throw new ProductAlreadyExistsException(product.getName(), product.getMyPrice());
    }

    public void reduceTotalSortAmount(String productName, Float boughtQuantity) {
        if (!productRepoPostgres.existsByNameIgnoreCase(productName)) throw new ProductNotExistException(productName);

        Float totalSortAmount = productRepoPostgres.getTotalSortAmount(productName);
/*
        if (totalSortAmount < 1 )
*/
        if ((totalSortAmount - boughtQuantity) >= 0) {
            productRepoPostgres.reduceTotalSortAmount(productName, boughtQuantity);
        } else {
            throw new NotEnoughSortException(productName,boughtQuantity-totalSortAmount);
        }
    }

    public void revertTotalSortAmount(String productName, Float boughtQuantity) {
        if (!productRepoPostgres.existsByNameIgnoreCase(productName)) throw new ProductNotExistException(productName);
        productRepoPostgres.reduceTotalSortAmount(productName, -boughtQuantity);

    }

    public Float getTotalAmount(String productName) {
        return productRepoPostgres.getTotalSortAmount(productName);
    }

    public void eraseRestOfProduct(String productName) {
        productRepoPostgres.eraseRestOfProduct(productName);
    }

    /**
     * For sake of compatibility with JsonFile version
     * must be provided map with Price-Name identifiers as a key
     **/
    @Override
    public List<Product> loadAllProducts() {
        List<ProductEntity> productEntities = productRepoPostgres.findAll();
        return productEntities.stream().map(productMapper::entityToProduct).collect(Collectors.toList());
    }

    public Product getProductByName(String productName) {
        if (productRepoPostgres.existsByNameIgnoreCase(productName)) {
            ProductEntity productEntity = productRepoPostgres.getByNameIgnoreCase(productName);
            return productMapper.entityToProduct(productEntity);
        } else throw new ProductNotExistException(productName);
    }

    public boolean existsByNameIgnoreCase(String productName){
        return productRepoPostgres.existsByNameIgnoreCase(productName);
    }

    @Override
    public void deleteProduct(String product) {
        productRepoPostgres.deleteByName(product);
    }
}
