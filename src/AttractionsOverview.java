import Objects.Attraction;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class AttractionsOverview {
    public static VBox getComponent(){

        VBox mainBox = new VBox();

        HBox listsContainerBox = new HBox();

        ListView<String> collumnNames = new ListView<>();
        collumnNames.getItems().add("Name");
        ListView<String> collumnPopularity = new ListView<>();
        collumnPopularity.getItems().add("Popularity");
        ListView<String> collumnPrice = new ListView<>();
        collumnPrice.getItems().add("Price");

        listsContainerBox.getChildren().addAll(collumnNames,collumnPopularity,collumnPrice);

        HashMap<UUID, Attraction> attractions = Planner.getSchedule().getAttractions();
        for (UUID key : attractions.keySet()) {
            collumnNames.getItems().add(attractions.get(key).getName());
            collumnPopularity.getItems().add(Integer.toString(attractions.get(key).getPopularity()));
            collumnPrice.getItems().add(Integer.toString(attractions.get(key).getPrice()));
        }

        mainBox.getChildren().add(listsContainerBox);
        //Planner.getSchedule().getAttractions();


        return mainBox;
    }
}

