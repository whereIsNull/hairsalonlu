package com.skynet.javafx.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Invoice extends SimpleEntity {

    public Float iVA;
    public BigDecimal total;
    public BigDecimal totalWithIVA;
    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    private List<InvoiceLine> lines = new ArrayList<>();
    private Date date;

    public List<InvoiceLine> getLines() {
        return lines;
    }

    public void setLines(List<InvoiceLine> lines) {
        this.lines = lines;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Float getiVA() {
        return iVA;
    }

    public void setiVA(Float iVA) {
        this.iVA = iVA;
    }

    public BigDecimal getTotalWithIVA() {
        return totalWithIVA;
    }

    public void setTotalWithIVA(BigDecimal totalWithIVA) {
        this.totalWithIVA = totalWithIVA;
    }

    public String getFirstLine() {
        return String.join("; ",
                lines.stream().map(l -> l.getProductName()).collect(Collectors.toList())
        );
    }

    public BigDecimal getPrize() {
        return this.lines.stream().reduce(new BigDecimal(0),
            (accumulator, line) -> {
                accumulator.add(line.getProductPrize());
                return accumulator;
            },
            (a1, a2) -> a1.add(a2)
        );
    }
}
