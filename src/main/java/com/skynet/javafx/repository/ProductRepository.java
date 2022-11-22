package com.skynet.javafx.repository;

import com.skynet.javafx.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
