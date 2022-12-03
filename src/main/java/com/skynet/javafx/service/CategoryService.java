package com.skynet.javafx.service;

import com.skynet.javafx.model.*;
import com.skynet.javafx.repository.CategoryAuditRepository;
import com.skynet.javafx.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements FrameService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryAuditRepository categoryAuditRepository;

    @Override
    public List<Category> getData() {
        ArrayList<Category> result = new ArrayList<Category>();
        categoryRepository.findAll().forEach(c -> result.add(c));
        return result;
    }

    @Override
    public void delete(Long id) {
        Optional<Category> category = this.categoryRepository.findById(id);
        if(category.isPresent()) {
            this.categoryRepository.deleteById(id);
            this.registerAudit(category.get(), Action.DELETE);
        }
    }

    public void save(Category category) {
        this.categoryRepository.save(category);
        registerAudit(category, Action.SAVE);
    }

    private void registerAudit(Category category, Action action) {
        ModelMapper converter = new ModelMapper();
        Date auditDate = new Date();
        CategoryAudit audit = converter.map(category, CategoryAudit.class);
        audit.setAuditDate(auditDate);
        audit.setAction(action);
        audit.setAuditDate(new Date());
        this.categoryAuditRepository.save(audit);
    }
}
