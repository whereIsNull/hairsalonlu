package com.skynet.javafx.controller;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.*;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.model.Invoice;
import com.skynet.javafx.model.InvoiceLine;
import com.skynet.javafx.utils.BigDecimalUtils;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.Sides;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
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

    private static final Integer lineWidth = 48;

    @Value("${company.iva:0.16}")
    Float iva;

    @Value("${company.name:Salón belleza}")
    String companyName;

    @FXML
    private BorderPane printerPane;
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
        PrintService[] printerServices = PrintServiceLookup.lookupPrintServices(null, null);
        printerList.getItems().addAll(Arrays.asList(printerServices));
        printerList.getItems().forEach(p -> {
            if(p.getName().contains("POS")) {
                printerList.getSelectionModel().select(p);
            }
        });
        // Create a Doc
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));
        aset.add(Sides.DUPLEX);

        double dpi = Screen.getPrimary().getDpi();
        textAreaTicket.setPrefWidth((80 * dpi)/25.4);
        textAreaTicket.setMinWidth((80 * dpi)/25.4);
        textAreaTicket.setMaxWidth((80 * dpi)/25.4);

        addButtonActions();
    }

    private String getInvoiceTicketText() {
        StringBuilder result = new StringBuilder(this.invoice.getDate().toString()).append("\n");
        invoice.getLines().forEach(l -> {
                Matcher m = Pattern.compile("(.{1,30}(\\W|$))").matcher(l.getProductName());
                StringBuilder b = new StringBuilder();
                List<StringBuilder> lines = new ArrayList<StringBuilder>();
                while (m.find()) {
                    lines.add(new StringBuilder(m.group()));
//                b.append(m.group()).append("\n");
                }
                String calculatedPrice = BigDecimalUtils.toString(l.getCalculatedPrice());
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
            ((Stage)btnCancel.getScene().getWindow()).close();
        });
    }

    public void render(Invoice invoice) {
        this.invoice = invoice;
        textAreaTicket.setText(getInvoiceTicketText());
    }

    private void printTicketCoffee() {
        PrintService printer = printerList.getSelectionModel().getSelectedItem();
        PrintService printService = PrinterOutputStream.getPrintServiceByName(printer.getName());
        EscPos escpos;
        try {
            String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(invoice.getDate());

            escpos = new EscPos(new PrinterOutputStream(printer));

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);

            addImage(escpos);
            escpos .write("Fecha: ")
                    .writeLF(subtitle, dateStr)
                    .feed(2);
            addLineToTicket(invoice, escpos);
//            invoice.getLines().forEach(invoiceLine -> addLineToTicket(invoiceLine, escpos));
            escpos.feed(2);
            escpos.writeLF("----------------------------------------") .feed(1);
            addTotals(invoice, escpos);
            escpos.feed(2);
            escpos.writeLF("----------------------------------------").feed(8).cut(EscPos.CutMode.FULL);

            escpos.feed(2);
            Style thanks = new Style()
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.writeLF(thanks, "GRACIAS POR SU VISITA");
            escpos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addImage(EscPos escpos) throws IOException {
        String strPath =  "images/luluNails_sin_color.png";
        URL url = this.getClass().getClassLoader() .getResource(strPath);
//        try {
//            RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
//            BufferedImage image = ImageIO.read(url);
//            Bitonal algorithm = new BitonalThreshold();
//            EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(image), algorithm);
//            escpos.write(imageWrapper, escposImage);
//            escpos.feed(5);
//        } catch (IOException e) {
            Style title = new Style()
                    .setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.writeLF(title,companyName).feed(3);
//        }
    }
    
    private void addLineToTicket(Invoice invoiceParam, EscPos escpos) {
        invoiceParam.getLines().forEach(l -> {
            Matcher m = Pattern.compile("(.{1,35}(\\W|$))").matcher(l.getProductName()+" (" + l.getAmount() + ")");
            StringBuilder b = new StringBuilder();
            List<StringBuilder> lines = new ArrayList<StringBuilder>();
            while (m.find()) {
                lines.add(new StringBuilder(m.group()));
            }
            String calculatedPrice = BigDecimalUtils.toString(l.getCalculatedPrice());
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

    private void addTotals(Invoice invoice, EscPos escpos) throws IOException {
        int lineWidthTotals = 40;
        int lineChars = ("Total neto: ".length() + BigDecimalUtils.toString(invoice.getTotalWithoutIVA()).length());
        escpos.write("Total neto: ");
        for(int i=0; i<(lineWidthTotals-lineChars); i++) {
            escpos.write(" ");
        }
        escpos.writeLF(BigDecimalUtils.toString(invoice.getTotalWithoutIVA()));

        BigDecimal addedIVA = invoice.getTotal().multiply(new BigDecimal(iva)).setScale(2, RoundingMode.UP);
        lineChars = (("I.V.A.(" + (iva*100) + "%): ").length() + BigDecimalUtils.toString(addedIVA).length());
        escpos.write("I.V.A.(" + (iva*100) + "%): ");
        for(int i=0; i<(lineWidthTotals-lineChars); i++) {
            escpos.write(" ");
        }
        escpos.writeLF(BigDecimalUtils.toString(addedIVA));

        if(invoice.getDiscount() != null && invoice.getDiscount().compareTo(new BigDecimal("0")) > 0) {
            BigDecimal totalDiscount = invoice.getDiscount()
                    .multiply(invoice.getTotal())
                    .multiply(BigDecimalUtils.toBigDecimal("0.01")).setScale(2, RoundingMode.UP);
            lineChars = ("Descuento ("+ BigDecimalUtils.toString(invoice.getDiscount()) +"%): ").length()
            + totalDiscount.toString().length();
            escpos.write("Descuento ("+ BigDecimalUtils.toString(invoice.getDiscount()) +"%): ");
            for(int i=0; i<(lineWidthTotals-lineChars); i++) {
                escpos.write(" ");
            }
            escpos.writeLF(BigDecimalUtils.toString(totalDiscount));
        }

        Style bold = new Style(escpos.getStyle()).setBold(true);
        escpos.write(bold, "TOTAL: ");
        lineChars = "TOTAL: ".length() + BigDecimalUtils.toString(invoice.getTotalWithDiscount()).length();
        for(int i=0; i<(lineWidthTotals-lineChars); i++) {
            escpos.write(" ");
        }
        escpos.writeLF(bold, BigDecimalUtils.toString(invoice.getTotalWithDiscount()));
    }
}
