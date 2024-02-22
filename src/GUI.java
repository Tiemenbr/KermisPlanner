import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;

public class GUI extends Application {

    private Planner planner;
    private FXMLLoader fxmlLoader;
    private CalendarController calendarController;
    public static void main(String[] args) {
        // On startup
        this.planner = new Planner();
        planner.init();
        this.fxmlLoader = new FXMLLoader(program.Main.class.getResource("/Calendar.fxml"));
        this.calendarController = new CalendarController();
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Kermis planner");

        TabPane tabpane = new TabPane();

        Tab planner = new Tab("Planner");
        Label plannerLabel = new Label("planner page");
        planner.setContent(plannerLabel);

        Tab schedule = new Tab("Schedule");
        Label scheduleLabel = new Label("page with visualization of the schedule");
        schedule.setContent(scheduleLabel);

        tabpane.getTabs().addAll(planner, schedule);
        tabpane.setTabClosingPolicy(UNAVAILABLE);

        Scene scene = new Scene(tabpane, 1200, 600);
        stage.setScene(scene);
        stage.show();
    }
}
