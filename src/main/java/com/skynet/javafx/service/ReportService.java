package com.skynet.javafx.service;

import com.skynet.javafx.model.Invoice;
import com.skynet.javafx.utils.ReportExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private InvoiceService invoiceService;

    @Value("${company.name}")
    private String companyName;
    @Value("${company.address}")
    private String address;
    @Value("${company.phone}")
    private String phone;
    @Value("${company.zip}")
    private String zip;
    @Value("${company.email}")
    private String email;

    public File generateHtml(int month, int year) {
        return this.generate(null, month, year, ReportExporter.HTML_EXPORTER);
    }

    public File generatePDF(int month, int year) {
        return this.generate(null, month, year, ReportExporter.PDF_EXPORTER);
    }

    public File generatePDF(String path, int month, int year) {
        return this.generate(path, month, year, ReportExporter.PDF_EXPORTER);
    }

    private File generate(String path, Integer month, Integer year, ReportExporter reportExporter) {
        List<Invoice> invoices = (List<Invoice>)invoiceService.getData(month, year);
        List<Map> invoicesList = invoices.stream().map(invoice -> {
            Map<String, Object> element = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            element.put("day", sdf.format(invoice.getDate()));
            element.put("price", invoice.getTotal().setScale(2).toString());
            element.put("totalWithoutIVA", invoice.getTotalWithoutIVA().setScale(2).toString());
            return element;
        }).collect(Collectors.toList());
        BigDecimal totalWithoutIVA = invoices.stream().reduce(
                new BigDecimal("0"),
                (accumulator, inv) -> accumulator.add(inv.getTotalWithoutIVA()),
                (a, b) -> a.add(b)
        );
        BigDecimal total = invoices.stream().reduce(
                new BigDecimal("0"),
                (accumulator, inv) -> accumulator.add(inv.getTotal()),
                (a, b) -> a.add(b)
        );
        InputStream employeeReportStream = getClass().getResourceAsStream("/jasper/Invoice.jrxml");
        File report = null;
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            String monthStr = symbols.getMonths()[month];
            Map<String, Object> params = new HashMap<>();
            params.put("companyName", companyName);
            params.put("zip", zip);
            params.put("address", address);
            params.put("phone", phone);
            params.put("email", email);
            params.put("month", monthStr.toUpperCase());
            params.put("year", year);
            params.put("totalWithoutIVA", totalWithoutIVA.setScale(2).toString());
            params.put("total", total.setScale(2).toString());
            params.put("invoicesList", invoicesList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JRAbstractExporter exporter = reportExporter.getExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//            File report = File.createTempFile("informe_facturacion_" + (monthStr) + "_" + year, ".html");
            report = new File(path, "informe_facturacion_" + (monthStr) + "_" + year + reportExporter.getExtension());
//            exporter.setExporterOutput( new SimpleHtmlExporterOutput(report));
            exporter.setExporterOutput(reportExporter.getExporterOutput(report));

            exporter.exportReport();
            return report;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(report != null) {
//                report.delete();
            }
        }
    }

}
