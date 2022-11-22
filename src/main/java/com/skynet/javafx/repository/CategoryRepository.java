package com.skynet.javafx.repository;

import com.skynet.javafx.model.Category;
import com.skynet.javafx.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

}
