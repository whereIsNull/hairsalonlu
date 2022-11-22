package com.skynet.javafx.views;

import com.skynet.javafx.jfxsupport.AbstractFxmlView;
import com.skynet.javafx.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView("/fxml/product.fxml")
@Scope("prototype")
public class ProductView extends AbstractFxmlView {
}
