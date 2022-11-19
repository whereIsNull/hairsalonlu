package com.skynet.javafx.service;

import com.skynet.javafx.model.Category;
import com.skynet.javafx.model.SimpleEntity;
import com.skynet.javafx.repository.CategoryRepository;
import com.skynet.javafx.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements FrameService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<? extends SimpleEntity> getData() {
        List<Category> result = new ArrayList<>();
        categoryRepository.findAll().forEach(cat -> result.add(cat));
        return result;
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
