package com.skynet.javafx.service;

import com.skynet.javafx.model.Product;
import com.skynet.javafx.model.SimpleEntity;
import com.skynet.javafx.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements FrameService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<? extends SimpleEntity> getData() {
        List<Product> result = new ArrayList<>();
        productRepository.findAll().forEach(cat -> result.add(cat));
        return result;
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
