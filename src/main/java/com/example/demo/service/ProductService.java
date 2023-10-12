package com.example.demo.service;

import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.product.exception.NotEnoughSortException;
import com.example.demo.businessLogic.product.exception.ProductAlreadyExistsException;
import com.example.demo.businessLogic.sale.exception.ProductNotExistException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.postgres.entity.ProductEntity;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepoPostgres productRepoPostgres;
    private final ProductMapper productMapper;


    public Product saveProduct(Product product) {
        if (!productRepoPostgres.existsByNameAndAdditionDate(product.getName(), product.getAdditionDate())) {
            ProductEntity productEntity = productMapper.productToEntity(product);
            productRepoPostgres.save(productEntity);
            return product;
        } else
            throw new ProductAlreadyExistsException(product.getName(), product.getMyPrice(), product.getAdditionDate().toString());
    }

    public void reduceTotalSortAmount(String productName, Float boughtQuantity) {
        if (!productRepoPostgres.existsByNameIgnoreCase(productName)) throw new ProductNotExistException(productName);

        Float totalSortAmount = productRepoPostgres.getTotalAmount(productName);

        Float reamingAmount = (totalSortAmount - boughtQuantity);
        if (reamingAmount >= 0) {
            productRepoPostgres.reduceTotalSortAmount(productName, boughtQuantity);
            setEraseDateIfThereIsNoMoreProduct(productName, reamingAmount);
        } else {
            throw new NotEnoughSortException(productName, boughtQuantity - totalSortAmount);
        }
    }

    private void setEraseDateIfThereIsNoMoreProduct(String productName, Float reamingAmount) {
        if (reamingAmount == 0) {
            //don't pay attention to method name - aim is to set date
            productRepoPostgres.eraseRestOfProduct(productName, LocalDate.now());
        }
    }

    public void revertTotalSortAmount(String productName, Float boughtQuantity) {
        if (!productRepoPostgres.existsByNameIgnoreCase(productName)) throw new ProductNotExistException(productName);
        productRepoPostgres.reduceTotalSortAmount(productName, -boughtQuantity);

    }

    public Float getTotalAmount(String productName) {
        return productRepoPostgres.getTotalAmount(productName);
    }

    public void eraseRestOfProduct(String productName) {
        productRepoPostgres.eraseRestOfProduct(productName, LocalDate.now());
    }

    /**
     * For sake of compatibility with JsonFile version
     * must be provided map with Price-Name identifiers as a key
     **/

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

    public boolean existsByNameIgnoreCase(String productName) {
        return productRepoPostgres.existsByNameIgnoreCase(productName);
    }

    public void deleteProductByNameAndAdditionDate(String product, LocalDate additionDate) {
        productRepoPostgres.deleteByNameAndAdditionDate(product, additionDate);
    }

}
