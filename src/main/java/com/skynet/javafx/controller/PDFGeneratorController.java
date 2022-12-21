package com.skynet.javafx.controller;

import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.service.ReportService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@FXMLController
public class PDFGeneratorController {

    private static Logger LOGGER = LoggerFactory.getLogger(PDFGeneratorController.class);
    private static final String PDF_HEADER = "data:application/pdf;base64,";

    @Autowired
    private ReportService reportService;

    @FXML
    private ComboBox<String> comboMonth;

    @FXML
    private ComboBox<Integer> comboYear;

    @FXML
    private Button buttonGenerate;

    @FXML
    private Button downloadReport;

    @FXML
    private Button sendReport;

    @FXML
    private WebView pdfViewer;

    private Integer monthNumber, yearNumber;

    @FXML
    private void initialize() {

        final DateFormatSymbols dfs = new DateFormatSymbols();

        // DateFormatSymbols in pre defined Locale
        this.comboMonth.setItems(FXCollections.observableList(Arrays.asList(dfs.getMonths())));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Integer> years = new ArrayList<>();
        for(int i=currentYear; i>(currentYear-50); i--) {
            years.add(i);
        }
        this.comboYear.setItems(FXCollections.observableList(years));
        WebEngine webEngine = pdfViewer.getEngine();
        URL url = this.getClass().getResource("/html/pdf-viewer.html");
        webEngine.load(url.toString());
//        webEngine.loadContent(content, "text/html");

        buttonGenerate.setOnAction(event -> {
            Date date = null;//put your month name in english here
            try {
                date = new SimpleDateFormat("MMMM", Locale.getDefault())
                        .parse(this.comboMonth.getSelectionModel().getSelectedItem());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                monthNumber = cal.get(Calendar.MONTH);
                yearNumber = comboYear.getSelectionModel().getSelectedItem();
                File report = reportService.generateHtml(monthNumber, yearNumber);

                String htmlContent = Files.readString(report.toPath());
                System.out.println(htmlContent);
                URL urlFile = report.toURL();
                webEngine.load(urlFile.toString());
                downloadReport.setDisable(false);
                sendReport.setDisable(false);
//                if(report != null) {
//                    report.delete();
//                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        addDownloadAction();
    }

    private void addDownloadAction() {
        downloadReport.setOnAction(event -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            final File selectedDirectory = directoryChooser.showDialog(downloadReport.getScene().getWindow());
            if (selectedDirectory != null) {
                selectedDirectory.getAbsolutePath();
            }
            reportService.generatePDF(selectedDirectory.getAbsolutePath(), monthNumber, yearNumber);
        });
    }

    private void addSendAction() {
        sendReport.setOnAction(event -> {

        });
    }

}
