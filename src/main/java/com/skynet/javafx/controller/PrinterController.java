package com.skynet.javafx.controller;

import com.skynet.javafx.jfxsupport.FXMLController;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

@FXMLController
public class PrinterController {

    @FXML
    private BorderPane printerPane;
    @FXML
    private ListView printerList;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAcept;

    @FXML
    private void initialize() {

        addButtonActions();

        ObservableSet<Printer> printers = Printer.getAllPrinters();
        printerList.getItems().addAll(printers);

//        final TextArea textArea = new TextArea();
//        Button button = new Button("Get all Printers");
//        //Get all Printers
//        button.setOnAction(event -> {
//            ObservableSet<Printer> printers = Printer.getAllPrinters();
//            for(Printer printer : printers) {
//                textArea.appendText(printer.getName()+"\n");
//            }
//        });
//        // Create the VBox with a 10px spacing
//        VBox root = new VBox(10);
//        List<Node> nodes = new ArrayList<>();
//        nodes.add(button);
//        nodes.add(textArea);
//        root.getChildren().add(button);
//        root.setPrefSize(400, 250);
//        root.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
//                "-fx-border-width: 2;" +
//                "-fx-border-insets: 5;" +
//                "-fx-border-radius: 5;" +
//                "-fx-border-color: blue;");
//        this.printerPane.getChildren().add(button);
//        this.printerPane.getChildren().add(textArea);
    }

    private void addButtonActions() {
        this.btnCancel.setOnAction(e -> {
            ((Stage)btnCancel.getScene().getWindow()).close();
        });
    }
}
