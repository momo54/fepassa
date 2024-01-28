package fr.gdd.fepassa;

import java.util.Comparator;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class FederationViewPanel extends BorderPane {

    ListView<String> listView = new ListView<>();
    List<String> members;
    Label title;

    public FederationViewPanel() {
        this.viewFederation();
        this.setCenter(listView); 
    }

    public void viewFederation() {
        // TODO Auto-generated method stub
        members=FedQuery.getFederation();
        members.sort(Comparator.naturalOrder());
        listView.getItems().clear();
        listView.getItems().addAll(members);
        title = new Label("Federation:"+members.size(),new FontIcon(Material.NETWORK_CELL));
        title.getStyleClass().addAll(Styles.TITLE_2);
        this.setTop(title);
    }

}
