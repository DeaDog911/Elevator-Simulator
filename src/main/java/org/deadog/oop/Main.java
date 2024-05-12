package org.deadog.oop;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.deadog.oop.controllers.ElevatorControl;
import org.deadog.oop.controllers.ElevatorController;
import org.deadog.oop.entities.House;
import org.deadog.oop.panels.ElevatorPanel;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        final double WIDTH = 600;
        final double HEIGHT = 400;

        try {
            Font.loadFont(GlyphsDude.class.getResource(FontAwesomeIconView.TTF_PATH).openStream(), 10.0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        House house = new House(2, 9);

        HBox root = new HBox(5);

        for (int i = 0; i < house.getEntrancesCount(); i++) {
            ElevatorControl elevatorControl = new ElevatorController();
            ElevatorPanel elevatorPanel = new ElevatorPanel(house.getFloorsCount(),WIDTH / 2, HEIGHT / 2, elevatorControl);
            root.getChildren().add(elevatorPanel);
        }

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setTitle("Лифта");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}