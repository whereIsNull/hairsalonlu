package com.skynet.javafx.controller;

import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.model.*;
import com.skynet.javafx.service.CategoryService;
import com.skynet.javafx.service.InvoiceService;
import com.skynet.javafx.service.ProductService;
import com.skynet.javafx.utils.TicketPrinter;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@FXMLController
public class InvoiceLinesController implements CrudController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceLinesController.class);

    @Value("${company.iva:0.16}")
    private Float iva;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TicketPrinter ticketPrinter;
    @FXML
    private TableView<InvoiceLine> invoiceLinesGrid;
    @FXML
    private Button btnNewLine;
    @FXML
    private Button btnRemoveLine;
//    @FXML
//    private Label totalLabel;
    @FXML
    private TextField ivaLabel;
    @FXML
    private TextField total;
    @FXML
    private TextField totalWithoutIVA;
    @FXML
    private Button btnCreateInvoice;
    @FXML
    private Button btnCancelInvoice;
    @FXML
    private ComboBox comboPaymentWay;
    @FXML
    private TextField receivedAmount;
    @FXML
    private TextField returnedValue;
    @FXML
    private TextField discount;
    @FXML
    private TextField totalWithdiscount;


    @FXML
    private void initialize() {

        List<Product> products = (List<Product>) productService.getData();
        List<Category> categories = categoryService.getData();
        ivaLabel.setText((iva*100)+"%");

        TableColumn<InvoiceLine, String> categoryColumn = new TableColumn<>("Categoría");
        TableColumn<InvoiceLine, String> productColumn = new TableColumn<>("Producto");

        // Category
        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<InvoiceLine, String>("category")
        );
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableList(categories.stream().map(Category::getCategory).collect(Collectors.toList())))
        );
        categoryColumn.setOnEditCommit(data -> {
            data.getRowValue().setCategory(data.getNewValue());
            List<Product> filteredProducts = FXCollections.observableList(products);
            if(data.getNewValue() != null && !data.getNewValue().trim().equals("")) {
                filteredProducts = filteredProducts.stream().filter(
                        p -> p.getCategory().equals(data.getNewValue())).collect(Collectors.toList()
                );
            }
            productColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                    FXCollections.observableList(filteredProducts.stream().map(Product::getProductName).collect(Collectors.toList())))
            );
            invoiceLinesGrid.refresh();
        });
        invoiceLinesGrid.getColumns().add(categoryColumn);

        // Product
        productColumn.setCellValueFactory(
                new PropertyValueFactory<InvoiceLine, String>("productName")
        );
        productColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableList(products.stream().map(Product::getProductName).collect(Collectors.toList())))
        );
        productColumn.setOnEditCommit(data -> {
            String productName = data.getNewValue();
            Product selectedProduct = products.stream().filter(p -> p.getProductName().equals(productName)).findFirst().get();
            data.getRowValue().setProductPrice(selectedProduct.getPrice());
            data.getRowValue().setProductName(selectedProduct.getProductName());
            if(data.getRowValue().getAmount() == null || data.getRowValue().getAmount() == 0) {
                data.getRowValue().setAmount(1);
            }
            calculateLinePrice(data.getRowValue());
            invoiceLinesGrid.getSelectionModel().getSelectedItem().setProductPrice(selectedProduct.getPrice());
            invoiceLinesGrid.getSelectionModel().getSelectedItem().setProductName(selectedProduct.getProductName());
            if(data.getRowValue().getProductPrice() != null && data.getRowValue().getAmount() != null) {
                invoiceLinesGrid.getSelectionModel().getSelectedItem().setCalculatedPrice(
                        invoiceLinesGrid
                                .getSelectionModel()
                                .getSelectedItem()
                                .getProductPrice()
                                .multiply(new BigDecimal(invoiceLinesGrid.getSelectionModel().getSelectedItem().getAmount()))
                );
            }
            invoiceLinesGrid.refresh();
            calculateTotals();
        });
        productColumn.setMinWidth(300.0);
        invoiceLinesGrid.getColumns().add(productColumn);

        // Precio unidad
        TableColumn<InvoiceLine, String> productPriceColumn = new TableColumn<>("Precio unidad");
        productPriceColumn.setCellValueFactory(
                new PropertyValueFactory<InvoiceLine, String>("productPrice")
        );
        productPriceColumn.setMinWidth(120.0);
        invoiceLinesGrid.getColumns().add(productPriceColumn);

        TableColumn<InvoiceLine, Integer> amountColumn = new TableColumn<>("Cantidad");
        amountColumn.setCellValueFactory(
                new PropertyValueFactory<InvoiceLine, Integer>("amount")
        );

        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return integer==null?"":integer.toString();
            }

            @Override
            public Integer fromString(String s) {
                return (s==null || s.isEmpty())?0:Integer.valueOf(s);
            }
        }));
        amountColumn.setMinWidth(50.0);
        amountColumn.setOnEditCommit(data -> {
            data.getRowValue().setAmount(Integer.valueOf(data.getNewValue()));
            calculateLinePrice(data.getRowValue());
            invoiceLinesGrid.getSelectionModel().getSelectedItem().setAmount(Integer.valueOf(data.getNewValue()));
            if(data.getRowValue().getProductPrice() != null && data.getRowValue().getAmount() != null) {
                invoiceLinesGrid.getSelectionModel().getSelectedItem().setCalculatedPrice(
                        invoiceLinesGrid
                                .getSelectionModel()
                                .getSelectedItem()
                                .getProductPrice()
                                .multiply(new BigDecimal(invoiceLinesGrid.getSelectionModel().getSelectedItem().getAmount()))
                );
            }
            invoiceLinesGrid.refresh();
            calculateTotals();
        });
        invoiceLinesGrid.getColumns().add(amountColumn);

        TableColumn<InvoiceLine, String> calculatedPriceColumn = new TableColumn<>("Precio total");
        calculatedPriceColumn.setCellValueFactory(
                new PropertyValueFactory<InvoiceLine, String>("calculatedPrice")
        );
        calculatedPriceColumn.setMinWidth(50.0);
        invoiceLinesGrid.getColumns().add(calculatedPriceColumn);

        invoiceLinesGrid.setEditable(true);

        receivedAmount.setDisable(true);
        configurePayment();

        configureButtons();

    }

    private void calculateTotals() {
        BigDecimal totalPrice = this.invoiceLinesGrid.getItems().stream().reduce(
                new BigDecimal(0.0),
                (accumulator, item) ->  accumulator.add(item.getCalculatedPrice()),
                (a, b) -> a.add(b)
        );
        this.total.setText(totalPrice.toString());
        this.totalWithoutIVA.setText(
                (totalPrice.multiply(new BigDecimal(1-iva)).setScale(2, RoundingMode.DOWN)).toString()
        );
        BigDecimal discountNumber = new BigDecimal("0");
        if(!StringUtils.isEmpty(this.discount.getText())) {
            discountNumber = new BigDecimal(this.discount.getText()).divide(new BigDecimal(100));
        }
        BigDecimal totalWithdiscountBD = new BigDecimal(1).subtract(discountNumber)
                .multiply(totalPrice).setScale(2, RoundingMode.DOWN);
        this.totalWithdiscount.setText(totalWithdiscountBD.toString());
        calculateReturnedValue();
    }

    void calculateLinePrice(InvoiceLine line) {
        if(line.getAmount() != null) {
            line.setCalculatedPrice(line.getProductPrice().multiply(new BigDecimal(line.getAmount())));
        }
    }

    @Override
    public void add() {
    }

    @FXML
    public void addInvoiceLine() {
        invoiceLinesGrid.getItems().add(new InvoiceLine());
    }

    @Override
    @FXML
    public void render(SimpleEntity entity) {
        Invoice invoice = (Invoice)entity;
        this.invoiceLinesGrid.setItems(FXCollections.observableList(invoice.getLines()));
//        this.totalLabel.setText(invoice.getTotalWithoutIVA().toString());
        this.total.setText(invoice.getTotal().toString());
        this.totalWithoutIVA.setText(invoice.getTotalWithoutIVA().toString());
        this.total.setText(invoice.getTotal().toString());
        this.totalWithdiscount.setText(
                invoice.getTotalWithDiscount()!=null?invoice.getTotalWithDiscount().toString():""
        );

        this.ivaLabel.setText(invoice.getiVA()!=null?(invoice.getiVA()*100)+"%":iva.toString());
        this.discount.setText(invoice.getDiscount()!=null?invoice.getDiscount().toString():"");

    }

    @Override
    public void save() {

    }

    private void configureButtons() {
        btnNewLine.setOnAction(event -> {
            this.invoiceLinesGrid.getItems().add(new InvoiceLine());
        });
        btnRemoveLine.setOnAction(event -> {
            this.invoiceLinesGrid.getItems().remove(this.invoiceLinesGrid.getSelectionModel().getSelectedItem());
            if(this.invoiceLinesGrid.getItems().isEmpty()) {
                clearAllComponents();
            } else {
                calculateTotals();
            }
        });
        btnCreateInvoice.setOnAction(event -> {
            Invoice invoice = new Invoice();
            invoice.setLines(new ArrayList<>(this.invoiceLinesGrid.getItems()));
            invoice.getLines().forEach(l -> l.setInvoice(invoice));
            invoice.setiVA(iva);
            invoice.setTotal(new BigDecimal(total.getText()));
            invoice.setTotalWithoutIVA(new BigDecimal(totalWithoutIVA.getText()));
            invoice.setDate(new Date());
            invoice.setDiscount((!StringUtils.isEmpty(this.discount.getText())?new BigDecimal(this.discount.getText()):new BigDecimal("0")));
            invoice.setTotalWithDiscount(new BigDecimal(this.totalWithdiscount.getText()));
            invoiceService.save(invoice);
            ticketPrinter.print(invoice);
            btnCreateInvoice.getScene().getWindow().hide();
        });
        btnCancelInvoice.setOnAction(event -> {
            btnCancelInvoice.getScene().getWindow().hide();
        });
    }

    private void clearAllComponents() {
        this.total.setText("");
        this.receivedAmount.setText("");
        this.totalWithoutIVA.setText("");
        this.returnedValue.setText("");

    }

    private void configurePayment() {
        comboPaymentWay.getItems().setAll(FXCollections.observableList(List.of("EFECTIVO", "TARJETA")));
        comboPaymentWay.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            receivedAmount.setDisable(!newValue.equals("EFECTIVO"));
            receivedAmount.setOnKeyTyped(e -> {
                calculateReturnedValue();
            });
        });

        discount.setOnKeyTyped(e -> {
            calculateTotals();
        });
    }

    private void calculateReturnedValue() {
        if(!StringUtils.isEmpty(receivedAmount.getText())) {
            BigDecimal received = new BigDecimal(receivedAmount.getText());
            BigDecimal totalAmount = new BigDecimal(totalWithdiscount.getText());
            if (received.compareTo(totalAmount) > 0) {
                returnedValue.setText(
                        received.subtract(totalAmount).setScale(2, RoundingMode.UP).toString()
                );
            }
        }
    }
}