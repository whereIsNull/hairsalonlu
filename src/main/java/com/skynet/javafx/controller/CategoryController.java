package com.skynet.javafx.controller;

import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.jfxsupport.GUIState;
import com.skynet.javafx.model.Category;
import com.skynet.javafx.model.Customer;
import com.skynet.javafx.model.SimpleEntity;
import com.skynet.javafx.service.CategoryService;
import com.skynet.javafx.service.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@FXMLController
public class CategoryController implements CrudController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @FXML
    private ButtonBarController buttonbarController;

    @FXML
    private TextField textCategoryName;
    @FXML
    private TextField textPrize;
    @FXML
    private Button addImage;

    private FileChooser fileChooser = new FileChooser();;

    @Autowired
    private CategoryService categoryService;

    private Category category;

    @FXML
    private void initialize() {
        logger.debug("initialize CustomerController");
        buttonbarController.setTarget(this);
        addImage.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(GUIState.getStage());
            if(selectedFile != null) {
                try {
                    Image img = new Image(new FileInputStream(selectedFile));
                    ImageView view = new ImageView(img);
                    view.setFitHeight(80);
                    view.setPreserveRatio(true);

                    addImage.setPrefSize(80, 80);
                    addImage.setGraphic(view);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void add() {
        // TODO Auto-generated method stub
    }

    @Override
    public void render(SimpleEntity entity) {
        category = (Category) entity;
        textCategoryName.setText(category.getServiceName());
        textPrize.setText(category.getPrize() != null? category.getPrize().toString(): "");
    }

    @Override
    public void save() {
        if (category == null) {
            category = new Category();
        }
        category.setServiceName(textCategoryName.getText());
        category.setPrize(new BigDecimal(textPrize.getText()));
        categoryService.save(category);
    }
}
