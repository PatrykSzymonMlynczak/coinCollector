package com.example.demo.postgres.service;

import com.example.demo.businessLogic.product.PriceNameId;
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

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("postgres")
@AllArgsConstructor
public class ProductService implements ProductRepo {

    private final ProductRepoPostgres productRepoPostgres;
    private final ProductMapper productMapper;

    @Override
    public Product saveProduct(Product product) {
        if(!productRepoPostgres.existsByNameAndPriceIgnoreCase(product.getName(),product.getMyPrice())){
            ProductEntity productEntity = productMapper.productToEntity(product);
            productRepoPostgres.save(productEntity);
            return product;
        }else throw new ProductAlreadyExistsException(product.getName(),product.getMyPrice());
    }

    /**
     * For sake of compatibility with JsonFile version
     * must be provided map with Price-Name identifiers as a key
     * */
    @Override
    public HashMap<PriceNameId, Product> loadAllProducts() {
        List<ProductEntity> productEntities = productRepoPostgres.findAll();
        List<Product> products = productEntities.stream().map(productMapper::entityToProduct).collect(Collectors.toList());

        HashMap<PriceNameId,Product> productsWithIdMap = new HashMap<>();
        for (Product product : products) {
            PriceNameId priceNameId = new PriceNameId(product.getMyPrice(), product.getName());
            productsWithIdMap.put(priceNameId, product);
        }

        return productsWithIdMap;
    }

    @Override
    public Product getProductByNameAndMyPrice(String productName, Float myPrice) {
        if(productRepoPostgres.existsByNameAndPriceIgnoreCase(productName,myPrice)){
            ProductEntity productEntity = productRepoPostgres.getByNameAndPriceIgnoreCase(productName,myPrice);
            return productMapper.entityToProduct(productEntity);
        }else throw new ProductNotExistException(productName, myPrice);
    }

    @Override
    public void deleteProduct(String product, Float myPrice) {
        productRepoPostgres.deleteByNameAndMyPrice(product, myPrice);
    }


}
