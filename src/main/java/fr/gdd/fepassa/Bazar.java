
package fr.gdd.fepassa;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;

public class Bazar extends Application {

    private static final Logger log = LoggerFactory.getLogger(Bazar.class);

    public static FindViewPanel findViewPanel = new FindViewPanel();
    public static ProductListViewPanel productListViewPanel = new ProductListViewPanel();
    public static ProductViewPanel productViewPanel = new ProductViewPanel();
    public static FederationViewPanel federationViewPanel = new FederationViewPanel();
    public static ReviewerViewPanel reviewerViewPanel = new ReviewerViewPanel();

    @Override
    public void start(Stage primaryStage) {

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        BorderPane root = new BorderPane();

        // Create the MenuBar
        MenuBar menuBar = new MenuBar();

        // Create Menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        // Create MenuItems
        MenuItem newItem = new MenuItem("New");

        // Create Submenus
        Menu openFederationSubMenu = new Menu("Open Federation");
        MenuItem shops50Item = new MenuItem("50shops");
        MenuItem shops100Item = new MenuItem("100shops");
        MenuItem shops200Item = new MenuItem("200shops");

        // Add Submenu Items to the Submenu
        openFederationSubMenu.getItems().addAll(shops50Item, shops100Item, shops200Item);

        // Add functionality to MenuItems (Optional)
        shops50Item.setOnAction(event -> {
            FedQuery.setFedup("jenadata50");
            federationViewPanel.viewFederation();
        });
        shops100Item.setOnAction(event -> {
            FedQuery.setFedup("jenadata100");
            federationViewPanel.viewFederation();
        });
        shops200Item.setOnAction(event -> {
            FedQuery.setFedup("jenadata200");
            federationViewPanel.viewFederation();
        });

        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(newItem, openFederationSubMenu, exitItem);

        exitItem.setOnAction(event -> System.exit(0));
        root.setTop(menuBar);

        VBox vb = new VBox();
        vb.getChildren().addAll(findViewPanel, federationViewPanel);
        VBox.setVgrow(federationViewPanel, Priority.ALWAYS);

        root.setLeft(vb);
        root.setRight(reviewerViewPanel);

        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.VERTICAL);
        sp.setDividerPositions(0.5f, 0.5f);
        sp.getItems().addAll(productListViewPanel, productViewPanel);

        root.setCenter(sp);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fepassa: The Federated Shop Demo");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
