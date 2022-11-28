package com.skynet.javafx.utils;


import com.skynet.javafx.controller.PrinterController;
import com.skynet.javafx.jfxsupport.AbstractFxmlView;
import com.skynet.javafx.model.Invoice;
import com.skynet.javafx.views.PrintView;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketPrinter {

    @Autowired
    private ApplicationContext context;

    public void print(Invoice invoice) {

        Stage stage = new Stage();
        AbstractFxmlView fxmlView = (AbstractFxmlView) context.getBean(PrintView.class);
        Scene scene = new Scene(fxmlView.getView());
        ((PrinterController)fxmlView.getFxmlLoader().getController()).render(invoice);


        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Impresoras");
        stage.show();
    }

//    public static void print() {
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
//        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
//        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
//        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
//        PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);
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
//            JOptionPane.showMessageDialog(null,"Error al imprimir: " + er.getMessage());
//        }
//    }
}
