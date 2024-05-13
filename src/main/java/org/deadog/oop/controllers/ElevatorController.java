package org.deadog.oop.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.deadog.oop.entities.Elevator;
import org.deadog.oop.entities.Floor;
import org.deadog.oop.entities.House;
import org.deadog.oop.entities.Passenger;
import org.deadog.oop.utils.ElevatorDirection;

import java.util.Random;

public class ElevatorController {
    private Elevator elevator;

    private VBox elevatorBox;

    private VBox panel;

    private GridPane grid;

    private House house;

    public ElevatorController(double width, double height, House house) {
        this.elevator = new Elevator(new Floor(1));
        this.house = house;
        this.panel = initializeElevatorPanel(width, height);
    }

    private void start() {

    }

    private boolean processCall(Floor floor, ElevatorDirection direction) {
        Floor destFloor = getRandomDestFloor(floor, direction);
        if (destFloor != null) {
            Passenger passenger = new Passenger(destFloor);
            house.getFloor(destFloor.getNumber()).getPassengerList().add(passenger);
            return true;
        }
        return false;
    }

    private Floor getRandomDestFloor(Floor startFloor, ElevatorDirection elevatorDirection) {
        Random random = new Random();
        Floor destFloor = null;
        if (elevatorDirection == ElevatorDirection.UP) {
            if (startFloor.getNumber() == house.getFloorsCount())
                return null;
            int num = random.nextInt(startFloor.getNumber() + 1, house.getFloorsCount() + 1);
            destFloor = new Floor(num);
        } else if (elevatorDirection == ElevatorDirection.DOWN) {
            if (startFloor.getNumber() == 1)
                return null;
            int num = random.nextInt(1, startFloor.getNumber() - 1);
            destFloor = new Floor(num);
        }
        return destFloor;
    }

    public VBox getPanel() {
        return panel;
    }

    private void moveToFloor(Floor floor) {
        grid.getChildren().remove(elevatorBox);
        grid.add(elevatorBox, 1, house.getFloorsCount() - floor.getNumber() + 1);
    }

    private VBox initializeElevatorPanel(double width, double height) {
        VBox elevatorPanel = new VBox();
        elevatorPanel.setPrefSize(width, height);
        elevatorPanel.setAlignment(Pos.CENTER);

        grid = initializeGrid(elevatorPanel);
        elevatorBox = initializeElevator();
        Button launchButton = new Button("Запуск");
        launchButton.setOnMouseClicked(e -> {
            start();
        });

        grid.add(elevatorBox, 1, house.getFloorsCount());
        grid.setPadding(new Insets(0, 0, 20, 0));

        elevatorPanel.getChildren().add(grid);
        elevatorPanel.getChildren().add(launchButton);
        return elevatorPanel;
    }

    private GridPane initializeGrid(VBox panel) {
        GridPane grid = new GridPane();
        for (int i = 0; i < 3; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints());
        }
        grid.setPrefWidth(panel.getPrefWidth());
        grid.setPrefHeight(panel.getPrefHeight());
        for (int i = 1; i <= house.getFloorsCount(); i++) {
            grid.getRowConstraints().add(new RowConstraints());

            Label label = new Label(String.valueOf(house.getFloorsCount() - i));
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(100);
            label.setPrefHeight(70);
            label.setFont(new Font(25));

            grid.add(label, 0, i);

            HBox controlBox = initializeControlBox(house.getFloorsCount()+1 - i);
            controlBox.prefWidthProperty().bind(grid.widthProperty());

            grid.add(controlBox, 2, i);
        }

        return grid;
    }

    private VBox initializeElevator() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("elevator");
        vBox.setPrefWidth(100);

        HBox upBox = new HBox();
        upBox.setAlignment(Pos.CENTER);
        HBox downBox = new HBox();
        downBox.setAlignment(Pos.CENTER);

        Label upLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_UP.characterToString());
        upLabelIcon.getStyleClass().addAll("up-arrow-passive", "font-awesome");

        Label upLabel = new Label("0");
        upLabel.setFont(new Font(15));

        upBox.getChildren().add(upLabelIcon);
        upBox.getChildren().add(upLabel);

        Label downLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_DOWN.characterToString());
        downLabelIcon.getStyleClass().addAll("down-arrow-passive", "font-awesome");

        Label downLabel = new Label("0");
        downLabel.setFont(new Font(15));

        downBox.getChildren().add(downLabelIcon);
        downBox.getChildren().add(downLabel);

        vBox.getChildren().add(upBox);
        vBox.getChildren().add(downBox);

        return vBox;
    }

    private HBox initializeControlBox(int floorNumber) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        hBox.setSpacing(5);

        Label upLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_UP.characterToString());
        upLabelIcon.getStyleClass().addAll("up-arrow-passive", "font-awesome", "control-button");

        Label upLabel = new Label("0");
        upLabel.setFont(new Font(15));

        upLabelIcon.setOnMouseClicked(mouseEvent -> {
            if (processCall(new Floor(floorNumber), ElevatorDirection.UP)) {
                upLabelIcon.getStyleClass().add("up-arrow-active");
                int up = Integer.parseInt(upLabel.getText());
                upLabel.setText(String.valueOf(up + 1));
            }
        });

        Label downLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_DOWN.characterToString());
        downLabelIcon.getStyleClass().addAll("down-arrow-passive", "font-awesome", "control-button");

        Label downLabel = new Label("0");
        downLabel.setFont(new Font(15));

        downLabelIcon.setOnMouseClicked(mouseEvent -> {
            if (processCall(new Floor(floorNumber), ElevatorDirection.DOWN)) {
                int down = Integer.parseInt(downLabel.getText());
                downLabel.setText(String.valueOf(down + 1));
                downLabelIcon.getStyleClass().add("down-arrow-active");
            }
        });

        hBox.getChildren().add(upLabelIcon);
        hBox.getChildren().add(upLabel);
        hBox.getChildren().add(downLabelIcon);
        hBox.getChildren().add(downLabel);
        hBox.setPrefHeight(70);

        return hBox;
    }
}