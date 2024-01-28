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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class ReviewerViewPanel extends BorderPane {
    private static final String Q09 = """
                        PREFIX rev: <http://purl.org/stuff/rev#>

            SELECT DISTINCT ?x WHERE {
                # const <http://www.ratingsite2.fr/Review10532>
                <%REVIEWER%> rev:reviewer ?x
            }
                        """;
    private Label reviewerLabel = new Label("No reviewer Selected", new FontIcon(Material.PERSON));
    private TableView<BindingSet> tableView = new TableView<>();

    public ReviewerViewPanel() {
        reviewerLabel.getStyleClass().addAll(Styles.TITLE_2);

        this.setTop(reviewerLabel);
        this.setCenter(tableView);

    }

    public void viewReviewer(String reviewer) {
        // TODO Auto-generated method stub
        reviewerLabel.setText(reviewer);
        String query = Q09.replaceAll("%REVIEWER%", reviewer);
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
