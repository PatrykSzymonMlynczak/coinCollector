package com.example.demo.service;

import com.example.demo.businessLogic.StaticProducts;
import com.example.demo.businessLogic.person.Person;
import com.example.demo.businessLogic.person.exception.PersonAlreadyExistsException;
import com.example.demo.businessLogic.person.exception.PersonNotExistsException;
import com.example.demo.businessLogic.product.Product;
import com.example.demo.businessLogic.sale.Sale;
import com.example.demo.dto.SaleDto;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.SaleMapper;
import com.example.demo.postgres.entity.PersonEntity;
import com.example.demo.postgres.repository.PersonRepoPostgres;
import com.example.demo.postgres.repository.ProductRepoPostgres;
import com.example.demo.postgres.repository.SaleRepoPostgres;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepoPostgres personRepoPostgres;
    private final SaleRepoPostgres saleRepoPostgres;
    private final ProductRepoPostgres productRepoPostgres;
    private final PersonMapper personMapper;
    private final SaleMapper saleMapper;
    private final ProductMapper productMapper;

    
    public Person savePerson(Person person) {
        PersonEntity personEntity = personMapper.personToEntity(person);
        if(!personRepoPostgres.existsByNameIgnoreCase(person.getName())) {
            personRepoPostgres.save(personEntity);
            return person;
        }else throw new PersonAlreadyExistsException(person.getName());
    }

    
    public Person getPerson(String name) {
        if(personRepoPostgres.existsByNameIgnoreCase(name)) {
            PersonEntity personEntity = personRepoPostgres.findByNameIgnoreCase(name);
            return personMapper.entityToPerson(personEntity);
        }else throw new PersonNotExistsException(name);
    }

    
    public List<Person> getAllPerson() {
        return personRepoPostgres.findAll().stream().map(personMapper::entityToPerson).collect(Collectors.toList());
    }

    public void setDebt(Float debt, String name){
        personRepoPostgres.updateDebt(debt,name);
    }

    //todo test
    public SaleDto payDebt(Float payedDebt, String personName){
        personRepoPostgres.reduceDebt(payedDebt,personName);

        Product payDebtProduct = new Product(StaticProducts.PAY_DEBT.name(), payedDebt);
        if(!productRepoPostgres.existsByNameIgnoreCase(payDebtProduct.getName())){
            productRepoPostgres.save(productMapper.productToEntity(payDebtProduct));
        }
        Person person = getPerson(personName);
        Product product = productMapper.entityToProduct(productRepoPostgres.getByNameIgnoreCase(payDebtProduct.getName()));
        Sale payDebtSale = new Sale(product, person, payedDebt);
        saleRepoPostgres.save(saleMapper.saleToEntity(payDebtSale));
        return saleMapper.saleToDto(payDebtSale);
    }
}
