package com.skynet.javafx.repository;

import com.skynet.javafx.model.Category;
import com.skynet.javafx.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
