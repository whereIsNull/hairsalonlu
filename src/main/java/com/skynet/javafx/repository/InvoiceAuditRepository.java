package com.skynet.javafx.repository;

import com.skynet.javafx.model.Invoice;
import com.skynet.javafx.model.InvoiceAudit;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceAuditRepository extends CrudRepository<InvoiceAudit, Long> {
}
