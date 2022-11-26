package com.skynet.javafx.service;

import com.skynet.javafx.model.Invoice;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void generate(int month, int year) {
        List<Invoice> invoices = (List<Invoice>)invoiceService.getData();
        List<Map> invoicesList = invoices.stream().map(invoice -> {
            Map<String, Object> element = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            element.put("day", sdf.format(invoice.getDate()));
            BigDecimal totalPrice = invoice.getLines().stream().reduce(
                    new BigDecimal("0.0"),
                    (accumulator, item) -> accumulator.add(item.getCalculatedPrice()),
                    (a1, a2) -> a1.add(a2)
            );
            element.put("price", totalPrice.setScale(2).toString());
            return element;
        }).collect(Collectors.toList());
        InputStream employeeReportStream = getClass().getResourceAsStream("/jasper/Invoice.jrxml");
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            String monthStr = symbols.getMonths()[month];
            Map<String, Object> params = new HashMap<>();
            params.put("month", monthStr);
            params.put("year", year);
            params.put("invoicesList", invoicesList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(
                    new SimpleOutputStreamExporterOutput("invoice.pdf"));

//            SimplePdfReportConfiguration reportConfig
//                    = new SimplePdfReportConfiguration();
//            reportConfig.setSizePageToContent(true);
//            reportConfig.setForceLineBreakPolicy(false);
//
//            SimplePdfExporterConfiguration exportConfig
//                    = new SimplePdfExporterConfiguration();
//            exportConfig.setAllowedPermissionsHint("PRINTING");
//
//            exporter.setConfiguration(reportConfig);
//            exporter.setConfiguration(exportConfig);

            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
