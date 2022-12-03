package com.skynet.javafx.service;

import com.skynet.javafx.model.*;
import com.skynet.javafx.repository.InvoiceAuditRepository;
import com.skynet.javafx.repository.InvoiceLIneAuditRepository;
import com.skynet.javafx.repository.InvoiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService implements FrameService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceAuditRepository invoiceAuditRepository;

    @Autowired
    private InvoiceLIneAuditRepository invoiceLineAuditRepository;

    @Override
    public List<? extends SimpleEntity> getData() {
        List<Invoice> result = new ArrayList<>();
        invoiceRepository.findAll().forEach(invoice -> result.add(invoice));
        return result;
    }

    public void save(Invoice invoice) {
        Invoice createdInvoice = invoiceRepository.save(invoice);
        registerAudit(createdInvoice, Action.INSERT);

    }

    @Override
    public void delete(Long id) {
        Optional<Invoice> deletedInvoice = this.invoiceRepository.findById(id);
        if(deletedInvoice.isPresent()) {
            invoiceRepository.deleteById(id);
            registerAudit(deletedInvoice.get(), Action.DELETE);
        }
    }

    public void findById(Long id) {
    }

    private void registerAudit(Invoice invoice, Action action) {
        ModelMapper converter = new ModelMapper();
        Date auditDate = new Date();
        InvoiceAudit audit = converter.map(invoice, InvoiceAudit.class);
        audit.setAuditDate(auditDate);
        audit.setAction(action);
        this.invoiceAuditRepository.save(audit);
        invoice.getLines().forEach(l -> {
            InvoiceLineAudit auditLine = converter.map(l, InvoiceLineAudit.class);
            auditLine.setAuditDate(auditDate);
            auditLine.setAction(action);
            this.invoiceLineAuditRepository.save(auditLine);
        });
    }
}
