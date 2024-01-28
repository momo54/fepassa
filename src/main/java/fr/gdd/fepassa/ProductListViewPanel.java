package fr.gdd.fepassa;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.units.qual.t;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductListViewPanel extends BorderPane {
    private Label title=new Label("Product List",new FontIcon(Material.LABEL));
    
    private TableView<BindingSet> tableView = new TableView<>();;
    private Button button1 = new Button("Product",new FontIcon(Material.SEARCH));
    private Button button2 = new Button("Detailed Product",new FontIcon(Material.SEARCH));;

    public ProductListViewPanel() {
        title.getStyleClass().addAll(Styles.TITLE_2);
        button1.setDefaultButton(true);
        button2.setDefaultButton(true);
        this.setTop(title);
        this.setCenter(tableView);

        // Add event handler for button click
        button1.setOnAction(event -> {
            // Perform the action when the button is clicked
            BindingSet selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                // Get the product name from the selected row
                String url = selectedRow.getValue("localProduct").stringValue();

                // Trigger your query using the selected product name
                System.out.println("Double-clicked on product: " + url);

                String productName=null;
                int lastIndex = url.lastIndexOf('/');
                if (lastIndex >= 0 && lastIndex < url.length() - 1) {
                    productName=url.substring(lastIndex + 1);
                    Bazar.productViewPanel.viewProduct("bsbm:"+productName,false);
                } else {
                    System.out.println("error on productName"); // or return null or the original URL based on your requirement
                }

            }
        });


        // Add event handler for button click
        button2.setOnAction(event -> {
            // Perform the action when the button is clicked
            BindingSet selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                // Get the product name from the selected row
                String url = selectedRow.getValue("localProduct").stringValue();

                // Trigger your query using the selected product name
                System.out.println("Double-clicked on product: " + url);

                String productName=null;
                int lastIndex = url.lastIndexOf('/');
                if (lastIndex >= 0 && lastIndex < url.length() - 1) {
                    productName=url.substring(lastIndex + 1);
                    Bazar.productViewPanel.viewProduct("bsbm:"+productName,true);
                } else {
                    System.out.println("error on productName"); // or return null or the original URL based on your requirement
                }

            }
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(button1, button2);
        hbox.setAlignment(Pos.CENTER); // Centrer les boutons horizontalement
        hbox.setSpacing(10); // Espacer les boutons de 10 pixels

        this.setBottom(hbox);
    }

    public void FindQuery(String query) {

        FedQuery q = new FedQuery();
        Pair<String, Long> p = q.SourceSelection(query);
        MultiSet<BindingSet> results = q.ExecuteWithFedX(p.getLeft());

        for (BindingSet bindingSet : results) {
            System.out.println(bindingSet);
            bindingSet.forEach(b -> System.out.println(b.getName() + " -> " + b.getValue()));
        }

        // Create columns
        // Clear existing columns
        tableView.getColumns().clear();

        for (String columnName : results.iterator().next().getBindingNames()) {
            TableColumn<BindingSet, String> column = new TableColumn<>(columnName);
                        column.setCellValueFactory(cellData -> {
                Value value = cellData.getValue().getValue(columnName);
                return new SimpleStringProperty((value != null) ? value.toString() : "");
            });
            tableView.getColumns().add(column);
        }

        ObservableList<BindingSet> data = FXCollections.observableArrayList(results);
        tableView.setItems(data);
        tableView.refresh();

    }

}
