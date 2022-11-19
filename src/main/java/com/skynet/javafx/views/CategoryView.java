package com.skynet.javafx.views;

import com.skynet.javafx.jfxsupport.AbstractFxmlView;
import com.skynet.javafx.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView("/fxml/category.fxml")
@Scope("prototype")
public class CategoryView extends AbstractFxmlView {
}
