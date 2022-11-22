package com.skynet.javafx.repository;

import com.skynet.javafx.model.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {


}
