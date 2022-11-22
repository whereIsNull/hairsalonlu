package com.skynet.javafx.service;

import com.skynet.javafx.model.Invoice;
import com.skynet.javafx.model.Product;
import com.skynet.javafx.model.SimpleEntity;
import com.skynet.javafx.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService implements FrameService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public List<? extends SimpleEntity> getData() {
        List<Invoice> result = new ArrayList<>();
        invoiceRepository.findAll().forEach(invoice -> result.add(invoice));
        return result;
    }

    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }

    public void findById(Long id) {
    }
}
