package com.skynet.javafx.service;

import com.skynet.javafx.model.*;
import com.skynet.javafx.repository.ProductAuditRepository;
import com.skynet.javafx.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements FrameService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAuditRepository productAuditRepository;

    @Override
    public List<? extends SimpleEntity> getData() {
        List<Product> result = new ArrayList<>();
        productRepository.findAll().forEach(cat -> result.add(cat));
        return result;
    }

    public void save(Product product) {
        Product createdProduct = productRepository.save(product);
        registerAudit(createdProduct, Action.DELETE);
    }

    @Override
    public void delete(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            productRepository.deleteById(id);
            registerAudit(product.get(), Action.DELETE);
        }
    }

    private void registerAudit(Product product, Action action) {
        ModelMapper converter = new ModelMapper();
        Date auditDate = new Date();
        ProductAudit audit = converter.map(product, ProductAudit.class);
        audit.setAuditDate(auditDate);
        audit.setAction(action);
        audit.setAuditDate(new Date());
        this.productAuditRepository.save(audit);
    }
}
