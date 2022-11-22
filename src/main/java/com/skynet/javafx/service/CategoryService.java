package com.skynet.javafx.service;

import com.skynet.javafx.model.Category;
import com.skynet.javafx.model.SimpleEntity;
import com.skynet.javafx.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements FrameService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getData() {
        ArrayList<Category> result = new ArrayList<Category>();
        categoryRepository.findAll().forEach(c -> result.add(c));
        return result;
    }

    @Override
    public void delete(Long id) {

    }

    public void save(Category category) {
        this.categoryRepository.save(category);
    }
}
