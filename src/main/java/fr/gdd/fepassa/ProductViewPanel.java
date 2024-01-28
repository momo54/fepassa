package fr.gdd.fepassa;

import java.util.Map;

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

public class ProductViewPanel extends BorderPane {

    private static final Map<String, String> templates = Map.of(
            "Q02",
            """
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
                                    """,

            "Q07",
            """
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
                                """,

            "Q05", """
                    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
                    PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
                    PREFIX owl: <http://www.w3.org/2002/07/owl#>


                    SELECT DISTINCT ?product ?localProductLabel WHERE {
                        ?localProduct rdfs:label ?localProductLabel .

                        ?localProduct bsbm:productFeature ?localProdFeature .
                        ?localProduct bsbm:productPropertyNumeric1 ?simProperty1 .
                        ?localProduct bsbm:productPropertyNumeric2 ?simProperty2 .


                        ?localProduct owl:sameAs ?product .
                        ?localProdFeature owl:sameAs ?prodFeature .


                        ?localProductXYZ bsbm:productFeature ?localProdFeatureXYZ .
                        ?localProductXYZ bsbm:productPropertyNumeric1 ?origProperty1 .
                        ?localProductXYZ bsbm:productPropertyNumeric2 ?origProperty2 .


                        # const bsbm:Product136030
                        ?localProductXYZ owl:sameAs %PRODUCT% .
                        ?localProdFeatureXYZ owl:sameAs ?prodFeature .


                        FILTER(bsbm:Product136030 != ?product)
                        # Values are pre-determined because we knew the boundaries from the normal distribution
                        FILTER(?simProperty1 < (?origProperty1 + 20) && ?simProperty1 > (?origProperty1 - 20))
                        FILTER(?simProperty2 < (?origProperty2 + 70) && ?simProperty2 > (?origProperty2 - 70))
                    }
                    ORDER BY ?product ?localProductLabel
                    LIMIT 5

                        """,

            "Q08",
            """
                    PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
                    PREFIX dc: <http://purl.org/dc/elements/1.1/>
                    PREFIX rev: <http://purl.org/stuff/rev#>
                    PREFIX foaf: <http://xmlns.com/foaf/0.1/>
                    PREFIX owl: <http://www.w3.org/2002/07/owl#>


                    SELECT DISTINCT ?title ?text ?reviewDate ?reviewer ?reviewerName ?rating1 ?rating2 ?rating3 ?rating4  WHERE {
                        # const bsbm:Product98654
                        ?review bsbm:reviewFor ?localProductXYZ .
                        ?localProductXYZ owl:sameAs %PRODUCT% .
                        ?review dc:title ?title .
                        ?review rev:text ?text .
                        FILTER(langMatches( lang(?text), "en" ))
                        ?review bsbm:reviewDate ?reviewDate .
                        ?review rev:reviewer ?reviewer .
                        ?reviewer foaf:name ?reviewerName .
                        OPTIONAL { ?review bsbm:rating1 ?rating1 . }
                        OPTIONAL { ?review bsbm:rating2 ?rating2 . }
                        OPTIONAL { ?review bsbm:rating3 ?rating3 . }
                        OPTIONAL { ?review bsbm:rating4 ?rating4 . }
                    }
                    ORDER BY ?title ?text ?reviewDate ?reviewer ?reviewerName ?rating1 ?rating2 ?rating3 ?rating4
                    LIMIT 20
                            """,
            "Q10", """
                            PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
                    PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
                    PREFIX dc: <http://purl.org/dc/elements/1.1/>
                    PREFIX owl: <http://www.w3.org/2002/07/owl#>


                    SELECT DISTINCT ?offer ?price WHERE {
                        # const bsbm:Product80530
                        ?offer bsbm:product ?localProductXYZ .
                        ?localProductXYZ owl:sameAs %PRODUCT% .
                        ?offer bsbm:vendor ?vendor .
                        #?offer dc:publisher ?vendor .
                        ?vendor bsbm:country <http://downlode.org/rdf/iso-3166/countries#US> .
                        ?offer bsbm:deliveryDays ?deliveryDays .
                        FILTER(?deliveryDays <= 3)
                        ?offer bsbm:price ?price .
                        ?offer bsbm:validTo ?date .
                        # const "2008-04-10T00:00:00"^^xsd:dateTime < ?date
                        FILTER (?date > "2008-04-10T00:00:00"^^xsd:dateTime )
                    }
                    ORDER BY ?offer ?price
                    LIMIT 10

                                """,
            "Q11", """
                     PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
                    SELECT DISTINCT ?property ?hasValue ?isValueOf WHERE {
                        # const <http://www.vendor6.fr/Offer886>
                        <%OFFER%> bsbm:product ?product .
                        { <%OFFER%> ?property ?hasValue }
                        UNION
                        { ?isValueOf ?property <%OFFER%> }
                    }
                                """);

    private Label productLabel = new Label("No Product Selected", new FontIcon(Material.LABEL));
    private TableView<BindingSet> tableView = new TableView<>();
    private Button reviewsProdButton = new Button("Reviewer", new FontIcon(Material.SEARCH));
    private Button offerButton = new Button("Offer", new FontIcon(Material.SEARCH));

    public ProductViewPanel() {
        productLabel.getStyleClass().addAll(Styles.TITLE_2);
        reviewsProdButton.setDefaultButton(true);
        offerButton.setDefaultButton(true);

        this.setTop(productLabel);
        this.setCenter(tableView);

        // Add event handler for button click
        reviewsProdButton.setOnAction(event -> {
            // Perform the action when the button is clicked
            BindingSet selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                // Get the product name from the selected row
                String reviewerURL = selectedRow.getValue("review").stringValue();

                // Trigger your query using the selected product name
                System.out.println("Double-clicked on reviewerURL: " + reviewerURL);
                Bazar.reviewerViewPanel.viewReviewer(reviewerURL);
            }
        });

        // Add event handler for button click
        offerButton.setOnAction(event -> {
            // Perform the action when the button is clicked
            BindingSet selectedRow = tableView.getSelectionModel().getSelectedItem();
            if (selectedRow != null) {
                // Get the product name from the selected row
                String reviewerURL = selectedRow.getValue("review").stringValue();

                // Trigger your query using the selected product name
                System.out.println("Double-clicked on reviewerURL: " + reviewerURL);
                Bazar.reviewerViewPanel.viewReviewer(reviewerURL);
            }
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(reviewsProdButton, offerButton);
        hbox.setAlignment(Pos.CENTER); // Centrer les boutons horizontalement
        hbox.setSpacing(10); // Espacer les boutons de 10 pixels

        this.setBottom(hbox);
    }

    public void viewProduct(String productName, String template) {
        System.out.println("viewProduct: " + productName);
        productLabel.setText(productName);
        String query = templates.get(template).replaceAll("%PRODUCT%", productName);
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

    public void viewOffer(String offer, String template) {
        productLabel.setText(offer);
        String query = templates.get(template).replaceAll("%OFFER%", offer);
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
