package fr.gdd.fepassa;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import atlantafx.base.theme.Styles;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.*;

public class FindViewPanel extends BorderPane {

    private static final String Q01_0 = """
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
            PREFIX bsbm-inst: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/>
            PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>


            SELECT DISTINCT ?localProduct ?label WHERE {
                ?localProduct rdfs:label ?label .

                # const bsbm:ProductType647
                ?localProduct rdf:type ?localProductType .
                ?localProductType owl:sameAs bsbm:ProductType647 .


                # const bsbm:ProductFeature8774
                ?localProduct bsbm:productFeature ?localProductFeature1 .
                ?localProductFeature1 owl:sameAs bsbm:ProductFeature8774 .


                # const bsbm:ProductFeature16935
                ?localProduct bsbm:productFeature ?localProductFeature2 .
                ?localProductFeature2 owl:sameAs bsbm:ProductFeature16935 .
                ?localProduct bsbm:productPropertyNumeric1 ?value1 .

                # const "744"^^xsd:integer < ?value1
                FILTER (?value1 > "744"^^xsd:integer)
            }
            ORDER BY ?localProduct ?label
            LIMIT 10
                """;

    private static String Q06_0 = """
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/>
            PREFIX owl: <http://www.w3.org/2002/07/owl#>


            SELECT DISTINCT ?localProduct ?label WHERE {
                ?localProduct rdfs:label ?label .
                ?localProduct rdf:type bsbm:Product .
                # const "pyrenees" in ?label
                FILTER regex(lcase(str(?label)), "pyrenees")
            }
                """;


    public FindViewPanel() {

        Label title = new Label("Search",new FontIcon(Material.SEARCH));
        title.getStyleClass().addAll(Styles.TITLE_2);
        this.setTop(title);

        VBox vbq1 = new VBox();

        GridPane gp = new GridPane();
        Label typeLabelq1 = new Label("ProductType : ");
        Label f1Labelq1 = new Label("ProductFeature1 : ");
        Label f2Labelq1 = new Label("ProductFeature2 : ");
        Label xLabelq1 = new Label("Value : ");

        TextField typeFieldq1 = new TextField("ProductType647");
        TextField f1Fieldq1 = new TextField("bsbm:ProductFeature8774");
        TextField f2Fieldq1 = new TextField("ProductFeature16935");
        TextField xFieldq1 = new TextField("744");
        gp.add(typeLabelq1, 0, 0);
        gp.add(f1Labelq1, 0, 1);
        gp.add(f2Labelq1, 0, 2);
        gp.add(xLabelq1, 0, 3);
        gp.add(typeFieldq1, 1, 0);
        gp.add(f1Fieldq1, 1, 1);
        gp.add(f2Fieldq1, 1, 2);
        gp.add(xFieldq1, 1, 3);

        Button runButtonq1 = new Button("Run",new FontIcon(Material.SEARCH));
        runButtonq1.setDefaultButton(true);

        runButtonq1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Run Q1");
                Bazar.productListViewPanel.FindQuery(Q01_0);
            }
        });

        vbq1.getChildren().addAll(gp, runButtonq1);

        VBox vbq3 = new VBox();
        TextField labelFieldq3 = new TextField("pyrenees");
        Button runButtonq3 = new Button("Run");
        vbq3.getChildren().addAll(labelFieldq3, runButtonq3);

        VBox vbq4 = new VBox();
        TextField labelFieldq4 = new TextField("pyrenees");
        Button runButtonq4 = new Button("Run",new FontIcon(Material.SEARCH));
        vbq4.getChildren().addAll(labelFieldq4, runButtonq4);

        VBox vbq6 = new VBox();
        TextField labelFieldq6 = new TextField("pyrenees");
        Button runButtonq6 = new Button("Run");
        runButtonq6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Run Q6");
                Bazar.productListViewPanel.FindQuery(Q06_0);
            }
        });
        vbq6.getChildren().addAll(labelFieldq6, runButtonq6);

        TabPane tabPane = new TabPane();
        Tab tabq1 = new Tab("Types", vbq1);
        Tab tabq3 = new Tab("NotIn", vbq3);
        Tab tabq4 = new Tab("Union", vbq4);
        Tab tabq6 = new Tab("KeyWord", vbq6);

        tabPane.getTabs().addAll(tabq1, tabq3, tabq4, tabq6);
        this.setCenter(tabPane);
    }

}
