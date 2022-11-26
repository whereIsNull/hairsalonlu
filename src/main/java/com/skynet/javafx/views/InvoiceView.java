package com.skynet.javafx.views;

import com.skynet.javafx.jfxsupport.AbstractFxmlView;
import com.skynet.javafx.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView("/fxml/invoice-lines_III.fxml")
@Scope("prototype")
public class InvoiceView extends AbstractFxmlView {
}
