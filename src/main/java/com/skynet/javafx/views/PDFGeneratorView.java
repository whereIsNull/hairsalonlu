package com.skynet.javafx.views;

import com.skynet.javafx.jfxsupport.AbstractFxmlView;
import com.skynet.javafx.jfxsupport.FXMLView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.context.annotation.Scope;

@FXMLView("/fxml/pdf-generator-view.fxml")
@Scope("prototype")
public class PDFGeneratorView extends AbstractFxmlView {

}
