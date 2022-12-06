package com.skynet.javafx.controller;

import java.util.Calendar;
import java.util.List;

import com.skynet.javafx.service.ReportService;
import com.skynet.javafx.views.PDFGeneratorView;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import com.skynet.javafx.config.PropertiesConfig;
import com.skynet.javafx.jfxsupport.FXMLController;
import com.skynet.javafx.service.FrameService;
import com.skynet.javafx.service.MenuItemService;
import com.skynet.javafx.views.FrameGridView;
import com.skynet.javafx.views.def.FrameGridDef;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

@FXMLController
public class MainController {

	private static String ADDITIONAL_TAB_TITLE = "     ";

	@Autowired
	private ApplicationContext context;

	@Autowired
	private PropertiesConfig config;

	@Autowired
	private MenuItemService menuItemService;

	@Autowired
	private ReportService reportService;

	@FXML
	private VBox mainPanel;

	@FXML
	private SplitPane mainSplitPanel;

	@FXML
	private TabPane tabPane;

	@FXML
	private MenuItem menuItemClose;

	@FXML
	private MenuItem monthReport;

	@Autowired
	private PDFGeneratorView pdfGeneratorView;
	
	TreeView<com.skynet.javafx.model.MenuItem> treeView;

	@FXML
	private void initialize() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		mainPanel.setPrefWidth(primaryScreenBounds.getWidth() * 0.8);
		mainPanel.setPrefHeight(primaryScreenBounds.getHeight() * 0.8);

		if (config.getJavafxMainToolbar()) {
			ToolBar toolbar = buildToolBar();
			mainPanel.getChildren().add(1, toolbar);
		}

		if (config.getJavafxMainTree()) {
			com.skynet.javafx.model.MenuItem rootMenuItem = menuItemService.getMenuItemRoot();
			TreeItem<com.skynet.javafx.model.MenuItem> rootNode = new TreeItem<>();
			rootNode.setValue(rootMenuItem);
			rootNode.setExpanded(rootMenuItem.getExpanded());
			treeView = new TreeView<>();
			treeView.setRoot(rootNode);
			buildTreeItems(rootNode);
			mainSplitPanel.getItems().add(0, treeView);
			treeView.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.ENTER) {
					onSelectItemAction(treeView);
				}
			});
			treeView.setOnMouseClicked((event) -> {
				if (event.getClickCount() == 2) {
					onSelectItemAction(treeView);
				}
			});
		}
		setOnCloseMenuItemAction();
	}

	public void onWindowShownEvent() {
		if (treeView != null) {
			MultipleSelectionModel<TreeItem<com.skynet.javafx.model.MenuItem>> msm = treeView.getSelectionModel();
			msm.select(treeView.getRoot());
			treeView.requestFocus();
		};
	}


	private void buildTreeItems(TreeItem<com.skynet.javafx.model.MenuItem> parentNode) {
		Long parentId = -1l;
		if (parentNode.getValue() != null) {
			parentId = parentNode.getValue().getId();
		}
		List<com.skynet.javafx.model.MenuItem> menuItems = menuItemService.getMenuItemsByParent(parentId);
		for (com.skynet.javafx.model.MenuItem item : menuItems) {
			TreeItem<com.skynet.javafx.model.MenuItem> itemNode = new TreeItem<>();
			itemNode.setValue(item);
			itemNode.setExpanded(item.getExpanded());
			if (!item.getImage().isEmpty()) {
				itemNode.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/" + item.getImage()))));
			}
			parentNode.getChildren().add(itemNode);
			buildTreeItems(itemNode);
		}
	}

	private void onSelectItemAction(TreeView<com.skynet.javafx.model.MenuItem> treeView) {
		TreeItem<com.skynet.javafx.model.MenuItem> item = treeView.getSelectionModel().getSelectedItem();
		if (item == null)
			return;
		int tabIndex = findTabIndex(item.getValue().getValue());
		if (tabIndex == -1) {
			tabPane.getTabs().contains((Object) item.getValue());
			showItemContent(item.getValue());
		} else {
			tabPane.getSelectionModel().select(tabIndex);
		}
	}

	private void showItemContent(com.skynet.javafx.model.MenuItem menuItem) {
		if (menuItem.getService() == null) {
			return;
		}

		if(menuItem.getKey().equals("master.reports")) {
			Tab tab = new Tab(menuItem.getValue() + ADDITIONAL_TAB_TITLE);
			tab.setContent(this.pdfGeneratorView.getView());
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
		} else {

			FrameGridView gridView = (FrameGridView) context.getBean(FrameGridView.class);
			FrameGridController controller = (FrameGridController) context.getBean(FrameGridController.class);
			gridView.setController(controller);

			Tab tab = new Tab(menuItem.getValue() + ADDITIONAL_TAB_TITLE);
			tab.setContent(gridView.getView());
			tabPane.getTabs().add(tab);
			tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);

			FrameService frameService = (FrameService) context.getBean(menuItem.getService());
			FrameGridDef frameGridDef = (FrameGridDef) context.getBean(menuItem.getGridDef());
			controller.initializeGrid(frameService, frameGridDef);
		}
	}

	private ToolBar buildToolBar() {
		ToolBar toolbar = new ToolBar();
		List<com.skynet.javafx.model.MenuItem> menuItems = menuItemService.getMenuItemsByParent(0l);
		for (com.skynet.javafx.model.MenuItem item : menuItems) {
			Button button = new Button();
			button.setTooltip(new Tooltip(item.getValue()));
			if (!item.getImage().isEmpty()) {
				button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/" + item.getImage()))));
			}
			button.setOnAction((event) -> { onToolBarButtonAction(item); });
			toolbar.getItems().add(button);
		}
		return toolbar;
	}

	private void onToolBarButtonAction(com.skynet.javafx.model.MenuItem item) {
		int tabIndex = findTabIndex(item.getValue());
		if (tabIndex == -1) {
			tabPane.getTabs().contains((Object) item.getValue());
			showItemContent(item);
		} else {
			tabPane.getSelectionModel().select(tabIndex);
		}
	}
	
	private int findTabIndex(String title) {
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			Tab tab = tabPane.getTabs().get(i);
			if (tab.getText().equals(title + ADDITIONAL_TAB_TITLE)) {
				return i;
			}
		}
		return -1;
	}

	private void setOnCloseMenuItemAction() {
		this.menuItemClose.setOnAction(event -> {
			Platform.exit();
		});
		this.monthReport.setOnAction(event -> {
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int year = Calendar.getInstance().get(Calendar.YEAR);
			this.reportService.generate(month, year);
		});
	}
}
