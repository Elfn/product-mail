package com.pm.productMail.dao;

import com.pm.productMail.entities.Product;
import com.pm.productMail.entities.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    public State findAllByState(String state);
}
