package com.skynet.javafx.repository;

import com.skynet.javafx.model.Invoice;
import org.h2.mvstore.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    @Query("select i from Invoice i where MONTH(i.date) = :month and YEAR(i.date) = :year")
    List<Invoice> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

}
