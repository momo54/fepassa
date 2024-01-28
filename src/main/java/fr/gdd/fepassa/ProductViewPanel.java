package fr.gdd.fepassa;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.lang3.tuple.Pair;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductViewPanel extends BorderPane {

    private static final String Q02 = """
            PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/>
            PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>


            SELECT DISTINCT ?label ?comment ?producer ?ProductFeatureLabel ?propertyTextual1 ?propertyTextual2 ?propertyTextual3 ?propertyNumeric1 ?propertyNumeric2 ?propertyTextual4 ?propertyTextual5 ?propertyNumeric4  WHERE {
                # const bsbm:Product116212
                ?localProduct owl:sameAs %PRODUCT% .
                ?localProduct rdfs:label ?label .
                ?localProduct rdfs:comment ?comment .
                ?localProduct bsbm:producer ?p .
                ?p rdfs:label ?producer .
                #?localProduct dc:publisher ?p .
                ?localProduct bsbm:productFeature ?localProductFeature1 .
                ?localProductFeature1 owl:sameAs ?ProductFeature1 .
                ?localProductFeature1 rdfs:label ?ProductFeatureLabel .
                ?localProduct bsbm:productPropertyTextual1 ?propertyTextual1 .
                ?localProduct bsbm:productPropertyTextual2 ?propertyTextual2 .
                ?localProduct bsbm:productPropertyTextual3 ?propertyTextual3 .
                ?localProduct bsbm:productPropertyNumeric1 ?propertyNumeric1 .
                ?localProduct bsbm:productPropertyNumeric2 ?propertyNumeric2 .
                OPTIONAL { ?localProduct bsbm:productPropertyTextual4 ?propertyTextual4 }
                OPTIONAL { ?localProduct bsbm:productPropertyTextual5 ?propertyTextual5 }
                OPTIONAL { ?localProduct bsbm:productPropertyNumeric4 ?propertyNumeric4 }
            }
                            """;

    private static final String Q07 = """
                    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX rev: <http://purl.org/stuff/rev#>
            PREFIX foaf: <http://xmlns.com/foaf/0.1/>
            PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>


            SELECT DISTINCT ?productLabel ?offer ?price ?vendor ?vendorTitle ?review ?revTitle ?reviewer ?revName ?rating1 ?rating2 WHERE {
                ?localProduct rdf:type bsbm:Product .
                # const bsbm:Product107874
                ?localProduct owl:sameAs %PRODUCT% .
                ?localProduct rdfs:label ?productLabel .


                OPTIONAL {
                    ?offer bsbm:product ?offerProduct .
                    ?offerProduct  owl:sameAs %PRODUCT% .
                    ?offer bsbm:price ?price .
                    ?offer bsbm:vendor ?vendor .
                    ?vendor rdfs:label ?vendorTitle .
                    ?vendor bsbm:country <http://downlode.org/rdf/iso-3166/countries#FR> .
                    ?offer bsbm:validTo ?date .


                    # const "2008-08-27T00:00:00"^^xsd:dateTime < ?date
                    FILTER (?date > "2008-08-27T00:00:00"^^xsd:dateTime )
                }
                OPTIONAL {
                    ?review bsbm:reviewFor ?reviewProduct .
                    ?reviewProduct owl:sameAs %PRODUCT% .
                    ?review rev:reviewer ?reviewer .
                    ?reviewer foaf:name ?revName .
                    ?review dc:title ?revTitle .
                    OPTIONAL { ?review bsbm:rating1 ?rating1 . }
                    OPTIONAL { ?review bsbm:rating2 ?rating2 . }
                }
            }

                        """;

    private Label productLabel = new Label("No Product Selected",new FontIcon(Material.LABEL));
    private TableView<BindingSet> tableView = new TableView<>();
    private Button similarProdButton = new Button("Similar Products",new FontIcon(Material.SEARCH));
    private Button availabilityProdButton = new Button("Availability",new FontIcon(Material.SEARCH));
    private Button reviewsProdButton = new Button("Reviews",new FontIcon(Material.SEARCH));
    private Button RecentRevProdButton = new Button("Recent Reviews",new FontIcon(Material.SEARCH));

    public ProductViewPanel() {
        productLabel.getStyleClass().addAll(Styles.TITLE_2);
        similarProdButton.setDefaultButton(true);
        availabilityProdButton.setDefaultButton(true);
        reviewsProdButton.setDefaultButton(true);
        RecentRevProdButton.setDefaultButton(true);

        this.setTop(productLabel);
        this.setCenter(tableView);

        // Add event handler for button click
        similarProdButton.setOnAction(event -> {
            // Perform the action when the button is clicked
            BindingSet selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                // Get the product name from the selected row
                String productName = selectedRow.getValue("product").stringValue();

                // Trigger your query using the selected product name
                System.out.println("Double-clicked on product: " + productName);
            }
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(similarProdButton, availabilityProdButton, reviewsProdButton, RecentRevProdButton);
        hbox.setAlignment(Pos.CENTER); // Centrer les boutons horizontalement
        hbox.setSpacing(10); // Espacer les boutons de 10 pixels

        this.setBottom(hbox);
    }

    public void viewProduct(String productName, boolean detailed) {
        productLabel.setText(productName);
        String query = null;
        if (detailed) {
            query = Q07.replaceAll("%PRODUCT%", productName);
        } else {
            query = Q02.replace("%PRODUCT%", productName);
        }
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
