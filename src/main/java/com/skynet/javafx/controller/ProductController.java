package com.skynet.javafx.controller;

import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.model.Category;
import com.skynet.javafx.model.Product;
import com.skynet.javafx.model.SimpleEntity;
import com.skynet.javafx.service.CategoryService;
import com.skynet.javafx.service.ProductService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@FXMLController
public class ProductController implements CrudController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Value("${hairSalonLu.iva:0.16}")
    private String iva;

    @FXML
    private ButtonBarController buttonbarController;
    @FXML
    private TextField textProductName;
    @FXML
    private TextField textPrice;
    @FXML
    private TextField textPriceWithoutIVA;
    @FXML
    private ComboBox<String> comboCategory;

    private FileChooser fileChooser = new FileChooser();;

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    private Product product;

    @FXML
    private void initialize() {
        logger.debug("initialize CustomerController");
        comboCategory.setItems(FXCollections.observableList(
                categoryService.getData().stream().map(Category::getCategory).collect(Collectors.toList()))
        );
        comboCategory.setEditable(true);
        textPrice.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldV, Boolean newV) {
                if(textPrice.getText() != null && !StringUtils.isEmpty(textPrice.getText())) {
                    textPriceWithoutIVA.setText(calculatePriceWithoutIva().toString());
                }
            }
        });
        buttonbarController.setTarget(this);
    }

    @Override
    public void add() {
        product = new Product();
    }

    @Override
    public void render(SimpleEntity entity) {
        product = (Product) entity;
        comboCategory.getSelectionModel().select(product.getCategory());
        textProductName.setText(product.getProductName());
        textPrice.setText(product.getPrice() != null? product.getPrice().toString(): "");
    }

    @Override
    public void save() {
        if (product == null) {
            product = new Product();
        }
        String categoryStr = comboCategory.getSelectionModel().getSelectedItem().trim();
        if(!categoryService.getData().stream().map(Category::getCategory).collect(Collectors.toList()).contains(categoryStr)) {
            Category category = new Category();
            category.setCategory(categoryStr);
            categoryService.save(category);
        }
        if(categoryStr == null) {
            return;
        }
        product.setCategory(categoryStr);
        product.setProductName(textProductName.getText());
        product.setPrice(new BigDecimal(textPrice.getText()));
        productService.save(product);
    }

    private BigDecimal calculatePriceWithoutIva() {
        BigDecimal price = new BigDecimal(textPrice.getText());
        BigDecimal ivaBD = new BigDecimal(iva);
        return price.divide(ivaBD.add(new BigDecimal(1)), 2, RoundingMode.HALF_DOWN);
    }
}
