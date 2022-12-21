package com.skynet.javafx.utils;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;

import java.io.File;
import java.util.function.Function;

public enum ReportExporter {

    PDF_EXPORTER(new JRPdfExporter(), ".pdf", f -> new SimpleOutputStreamExporterOutput(f)),
    HTML_EXPORTER(new HtmlExporter(), ".html", f -> new SimpleHtmlExporterOutput(f));

    ReportExporter(JRAbstractExporter exporter, String extension, Function<File, ExporterOutput> exporterOutput) {
        this.exporter = exporter;
        this.extension = extension;
        this.exporterOutput = exporterOutput;
    }


    JRAbstractExporter exporter;
    String extension;
    Function<File, ExporterOutput>  exporterOutput;

    public JRAbstractExporter getExporter() {
        return exporter;
    }

    public String getExtension() {
        return extension;
    }

    public ExporterOutput getExporterOutput(File file) {
        return this.exporterOutput.apply(file);
    }

}