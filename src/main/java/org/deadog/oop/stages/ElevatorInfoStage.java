package org.deadog.oop.stages;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.deadog.oop.entities.Floor;
import org.deadog.oop.entities.Passenger;

import java.util.List;

public class ElevatorInfoStage extends Stage {
    private Stage infoStage;

    private VBox infoPanel;

    public ElevatorInfoStage(int num) {
        this.infoPanel = initializeInfoPanel(num);
        initializeInfoStage();
    }

    private VBox initializeInfoPanel(int num) {
        VBox infoPanel = new VBox();
        infoPanel.setId("infoPanel");
        infoPanel.setSpacing(10);
        infoPanel.setPadding(new Insets(10));

        Label elevatorNumber = new Label("Лифт №" + num);
        Label currentFloorLabel = new Label("Текущий этаж: 1");
        currentFloorLabel.setId("currentFloorLabel");

        Label passengersCountLabel = new Label("Количество пассажиров: 0");
        passengersCountLabel.setId("passengersCountLabel");

        Label activeLabel = new Label("Состояние лифта: Неактивен");
        activeLabel.setId("activeLabel");

        VBox passengersListBox = new VBox();
        passengersListBox.setId("passengersListBox");
        passengersListBox.setSpacing(5);

        infoPanel.getChildren().addAll(elevatorNumber, currentFloorLabel, activeLabel, passengersCountLabel, passengersListBox);

        return infoPanel;
    }

    private void initializeInfoStage() {
        infoStage = new Stage();
        infoStage.setTitle("Информационное окно");
        Scene scene = new Scene(infoPanel, 300, 400);
        infoStage.setScene(scene);
        infoStage.show();
    }

    public void updateInfoPanel(Floor floor, List<Passenger> passengers, boolean active) {
        Label currentFloorLabel = (Label) infoPanel.lookup("#currentFloorLabel");
        Label passengersCountLabel = (Label) infoPanel.lookup("#passengersCountLabel");
        Label activeLabel = (Label) infoPanel.lookup("#activeLabel");
        VBox passengersListBox = (VBox) infoPanel.lookup("#passengersListBox");

        currentFloorLabel.setText("Текущий этаж: " + floor.getNumber());
        passengersCountLabel.setText("Количество пассажиров: " + passengers.size());
        activeLabel.setText("Состояние лифта: " + (active ? "Активен" : "Неактивен"));

        passengersListBox.getChildren().clear();
        for (Passenger passenger : passengers) {
            Label passengerLabel = new Label("Пассажир на этаж: " + passenger.getDestinationFloor().getNumber());
            passengersListBox.getChildren().add(passengerLabel);
        }
    }
}
