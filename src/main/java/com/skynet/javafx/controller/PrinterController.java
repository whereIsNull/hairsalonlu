package com.skynet.javafx.controller;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.model.Invoice;
import com.skynet.javafx.model.InvoiceLine;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.Sides;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FXMLController
public class PrinterController {

    @Value("${company.iva:0.16}")
    Float iva;

    @Value("${company.name:Salón belleza}")
    String companyName;

    @FXML
    private BorderPane printerPane;
//    @FXML
//    private ListView<Printer> printerList;
    @FXML
    private ListView<PrintService> printerList;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAccept;
    @FXML
    private TextArea textAreaTicket;

    private Invoice invoice;

    @FXML
    private void initialize() {
//        ObservableSet<Printer> printers = Printer.getAllPrinters();
//        printerList.getItems().addAll(printers);
        PrintService[] printerServices = PrintServiceLookup.lookupPrintServices(null, null);
        printerList.getItems().addAll(Arrays.asList(printerServices));
        // Create a Doc
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));
        aset.add(Sides.DUPLEX);

        addButtonActions();

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

    private String getInvoiceTicketText() {
        StringBuilder result = new StringBuilder(this.invoice.getDate().toString()).append("\n");
        int lineWidth = 60;
        invoice.getLines().forEach(l -> {
                Matcher m = Pattern.compile("(.{1,20}(\\W|$))").matcher(l.getProductName());
                StringBuilder b = new StringBuilder();
                List<StringBuilder> lines = new ArrayList<StringBuilder>();
                while (m.find()) {
                    lines.add(new StringBuilder(m.group()));
//                b.append(m.group()).append("\n");
                }
                String calculatedPrice = l.getCalculatedPrice().toString();
                StringBuilder lastLine = lines.get(lines.size()-1);
                int numberOfDots = lineWidth - calculatedPrice.length() - lastLine.toString().length();
                System.out.println("dots: " + numberOfDots);
            for(int i=0; i<numberOfDots; i++) {
                lastLine.append(".");
            }
            lastLine.append(calculatedPrice);
            result.append(String.join("\n", lines)).append("\n");
        });

        System.out.println(result.toString());
        return result.toString();
    }

    private void addButtonActions() {
        this.btnCancel.setOnAction(e -> {
            ((Stage)btnCancel.getScene().getWindow()).close();
        });
        this.btnAccept.setOnAction(e -> {
            this.printTicketCoffee();
        });
    }

    public void render(Invoice invoice) {
        this.invoice = invoice;
        textAreaTicket.setText(getInvoiceTicketText());
    }

    public void printTicket() {

//         con javafx
//        Printer selectedPrinter = this.printerList.getSelectionModel().getSelectedItem();
//        selectedPrinter.createPageLayout(Paper.A6, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
//        PrinterJob job = PrinterJob.createPrinterJob(selectedPrinter);
//        try {
//            //El metodo print imprime
//            job.printPage(textAreaTicket);
//            job.endJob();
//        } catch (Exception er) {
//            er.printStackTrace();
//        } finally {
//            ((Stage)btnCancel.getScene().getWindow()).close();
//        }

        //con printservices
//        PrintService printer = printerList.getSelectionModel().getSelectedItem();
//        Arrays.stream(printer.getSupportedDocFlavors()).forEach(f -> {
//            System.out.print("media type : "+f.getMediaType());
//            System.out.println(" - mime type : "+f.getMimeType());
//        });
//        DocPrintJob job = printer.createPrintJob();
//        try {
//            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//
//            aset.add(new Copies(1));
//            aset.add(Sides.DUPLEX);
//
//            DocFlavor myFlavor = DocFlavor.INPUT_STREAM.TEXT_PLAIN_UTF_8;
////            String data = this.textAreaTicket.getText();
//            ByteArrayInputStream data = new ByteArrayInputStream(this.textAreaTicket.getText().getBytes(StandardCharsets.UTF_8));
//            Doc myDoc = new SimpleDoc(data, myFlavor, null);
//            job.print(myDoc, aset);
//        } catch (PrintException pe) {
//            pe.printStackTrace();
//        }
    }


//    public void printNotJavaFX() {
//        //Ticket attribute content
//        String contentTicket = "VINATERIA {{nameLocal}}\n"+
//                "EXPEDIDO EN: {{expedition}}\n"+
//                "DOMICILIO CONOCIDO MERIDA, YUC.\n"+
//                "=============================\n"+
//                "MERIDA, XXXXXXXXXXXX\n"+
//                "RFC: XXX-020226-XX9\n"+
//                "Caja # {{box}} - Ticket # {{ticket}}\n"+
//                "LE ATENDIO: {{cajero}}\n"+
//                "{{dateTime}}\n"+
//                "=============================\n"+
//                "{{items}}\n"+
//                "=============================\n"+
//                "SUBTOTAL: {{subTotal}}\n"+
//                "IVA: {{tax}}\n"+
//                "TOTAL: {{total}}\n\n"+
//                "RECIBIDO: {{recibo}}\n"+
//                "CAMBIO: {{change}}\n\n"+
//                "=============================\n"+
//                "GRACIAS POR SU COMPRA...\n"+
//                "ESPERAMOS SU VISITA NUEVAMENTE {{nameLocal}}\n"+
//                "\n"+
//                "\n";
//
//
//        //Especificamos el tipo de dato a imprimir
//        //Tipo: bytes; Subtipo: autodetectado
//        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//
//        //Aca obtenemos el servicio de impresion por defatul
//        //Si no quieres ver el dialogo de seleccionar impresora usa esto
//        //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
//
//
//        //Con esto mostramos el dialogo para seleccionar impresora
//        //Si quieres ver el dialogo de seleccionar impresora usalo
//        //Solo mostrara las impresoras que soporte arreglo de bits
////        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
////        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
////        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
////        PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);
//        PrintService service = this.printerList.getSelectionModel().getSelectedItem();
//
//        //Creamos un arreglo de tipo byte
//        byte[] bytes;
//
//        //Aca convertimos el string(cuerpo del ticket) a bytes tal como
//        //lo maneja la impresora(mas bien ticketera :p)
//        bytes = contentTicket.getBytes();
//
//        //Creamos un documento a imprimir, a el se le appendeara
//        //el arreglo de bytes
//        Doc doc = new SimpleDoc(bytes,flavor,null);
//
//        //Creamos un trabajo de impresión
//        DocPrintJob job = service.createPrintJob();
//
//        //Imprimimos dentro de un try de a huevo
//        try {
//            //El metodo print imprime
//            job.print(doc, null);
//        } catch (Exception er) {
//            er.printStackTrace();
//        }
//    }

    private void printTicketCoffee() {
        PrintService printer = printerList.getSelectionModel().getSelectedItem();
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printer.getName());
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printer));

            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);

            String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(invoice.getDate());

            escpos.writeLF(title,companyName)
                    .feed(3)
                    .write("Fecha: ")
                    .writeLF(subtitle, dateStr)
                    .feed(3);
            invoice.getLines().forEach(invoiceLine -> addLineToTicket(invoiceLine, escpos));
            escpos.writeLF("----------------------------------------")
                    .feed(2);
            escpos.writeLF("Total neto:                         " + invoice.getTotalWithoutIVA().toString());
            escpos.writeLF("I.V.A.(" + (iva*100) + "%):         " +
                    invoice.getTotal().multiply(new BigDecimal(iva)).setScale(2, RoundingMode.UP));
            if(invoice.getDiscount() != null) {
                escpos.writeLF("Descuento ("+ invoice.getDiscount().toString() +")       "
                        + (invoice.getDiscount().multiply(invoice.getDiscount())));
            }
            escpos.writeLF(bold, "TOTAL                              " + invoice.getTotalWithDiscount().toString())
                    .writeLF("----------------------------------------")
                    .feed(8)
                    .cut(EscPos.CutMode.FULL);

            escpos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void addLineToTicket(InvoiceLine invoiceLine, EscPos escpos) {
        int lineWidth = 60;
        invoice.getLines().forEach(l -> {
            Matcher m = Pattern.compile("(.{1,20}(\\W|$))").matcher(l.getProductName());
            StringBuilder b = new StringBuilder();
            List<StringBuilder> lines = new ArrayList<StringBuilder>();
            while (m.find()) {
                lines.add(new StringBuilder(m.group()));
            }
            String calculatedPrice = l.getCalculatedPrice().toString();
            StringBuilder lastLine = lines.get(lines.size()-1);
            int numberOfDots = lineWidth - calculatedPrice.length() - lastLine.toString().length();
            System.out.println("dots: " + numberOfDots);
            for(int i=0; i<numberOfDots; i++) {
                lastLine.append(".");
            }
            lastLine.append(calculatedPrice);
            lines.stream().forEach(lStr -> {
                try {
                    escpos.writeLF(lStr.toString());
                    System.out.println(lStr.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
