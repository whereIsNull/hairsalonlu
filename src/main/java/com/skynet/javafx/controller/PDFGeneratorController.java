package com.skynet.javafx.controller;

import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.service.ReportService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@FXMLController
public class PDFGeneratorController {

    @Autowired
    private ReportService reportService;

    @FXML
    private ComboBox<String> comboMonth;

    @FXML
    private ComboBox<Integer> comboYear;

    @FXML
    private Button buttonGenerate;

    @FXML
    private WebView pdfViewer;

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
                Integer monthNumber = cal.get(Calendar.MONTH);
                Integer yearNumber = comboYear.getSelectionModel().getSelectedItem();

                reportService.generate(monthNumber, yearNumber);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });
    }
}
