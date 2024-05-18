package org.deadog.oop.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.deadog.oop.entities.Elevator;
import org.deadog.oop.entities.Floor;
import org.deadog.oop.entities.House;
import org.deadog.oop.entities.Passenger;
import org.deadog.oop.utils.ElevatorDirection;
import org.xml.sax.helpers.AttributesImpl;

import javax.print.attribute.standard.Destination;
import java.sql.Time;
import java.util.*;

public class ElevatorController {
    private Elevator elevator;

    private VBox elevatorBox;

    private VBox panel;

    private GridPane grid;

    private House house;

    private boolean active;

    private List<Floor> callsQuery;

    public ElevatorController(double width, double height, House house) {
        this.elevator = new Elevator(house.getFloor(1));
        this.house = house;
        this.panel = initializeElevatorPanel(width, height);
        this.active = false;
        this.callsQuery = new ArrayList<>();
    }

    public VBox getPanel() {
        return panel;
    }

    private void start() {
        if (!callsQuery.isEmpty()) {
            active = true;
            Timeline timeline = new Timeline();
            Duration totalDelay = Duration.seconds(elevator.getTravelTime());

            // Обрабатываем очередной вызов
            Floor floor = callsQuery.get(0);
            setDestination(floor);
            Floor nextFloor = floor;
            do {
                nextFloor = getFloorOnWay(nextFloor);
                System.out.println(nextFloor.getNumber());
                if (nextFloor.equals(floor.getNumber()))
                    elevator.setDirection(ElevatorDirection.NONE);

                // Прибытие на этаж
                processMoving(nextFloor);
                int up = elevator.getPassengersCountUp();
                int down = elevator.getPassengersCountDown();
                Floor finalNextFloor = nextFloor;
                KeyFrame keyFrame = new KeyFrame(totalDelay, event -> {
                    moveToFloor(finalNextFloor);
                    updateControls(finalNextFloor);
                    updateElevatorBox(up, down);
                });
                totalDelay = totalDelay.add(Duration.seconds(elevator.getTravelTime()));
                timeline.getKeyFrames().add(keyFrame);

                System.out.println("dest");
                print(elevator.getDestinationFloors());

                if (!elevator.getDestinationFloors().isEmpty()) {
                    nextFloor = elevator.getDestinationFloors().get(0);
                }

                if (!callsQuery.isEmpty()) {
                    nextFloor = callsQuery.get(0);
                }
            } while (!callsQuery.isEmpty() || elevator.getPassengersCount() != 0);
            timeline.setOnFinished(event -> {
                active = false;
            });
            timeline.play();
        }
    }

    private void print(List<Floor> floors) {
        for (Floor floor : floors) {
            System.out.print(floor.getNumber() + ",");
        }
        System.out.println();
    }

    public Floor getNextFloor(Floor floor) {
        return floor;
    }

    public void setDestination(Floor floor) {
        if (floor.getNumber() > elevator.getCurrentFloor().getNumber())
            elevator.setDirection(ElevatorDirection.UP);
        else if (floor.getNumber() < elevator.getCurrentFloor().getNumber())
            elevator.setDirection(ElevatorDirection.DOWN);
        else
            elevator.setDirection(ElevatorDirection.NONE);
    }

    private Floor getFloorOnWay(Floor destFloor) {
        switch (elevator.getDirection()) {
            case UP: {
                int minNum = destFloor.getNumber();
                for (Floor call : callsQuery) {
                    if (call.getNumber() > elevator.getCurrentFloor().getNumber()
                            && destFloor.getNumber() > call.getNumber())
                        minNum = Math.min(call.getNumber(), minNum);
                }
                if (!elevator.getDestinationFloors().isEmpty())
                    minNum = Math.min(elevator.getDestinationFloors().get(0).getNumber(), minNum);
                return house.getFloor(minNum);
            } case DOWN: {
                int maxNum = destFloor.getNumber();
                for (Floor call : callsQuery) {
                    if (call.getNumber() < elevator.getCurrentFloor().getNumber()
                            && destFloor.getNumber() < call.getNumber())
                        maxNum = Math.max(call.getNumber(), maxNum);
                }
                if (!elevator.getDestinationFloors().isEmpty())
                    maxNum = Math.max(elevator.getDestinationFloors().get(0).getNumber(), maxNum);
                return house.getFloor(maxNum);
            } case NONE:
                return destFloor;
            default:
                return null;
        }
    }

    public void processMoving(Floor floor) {
        elevator.setCurrentFloor(floor);
        elevator.removePassengers(floor);

        if (elevator.getDirection() == ElevatorDirection.UP ||
                elevator.getDirection() == ElevatorDirection.NONE && floor.getDirection() == ElevatorDirection.UP) {
            List<Passenger> passengersUp = floor.getPassengersUp();
            while (!passengersUp.isEmpty() && elevator.getPassengersCount() < elevator.getCapacity()) {
                elevator.getPassengers().add(passengersUp.remove(0));
            }
        } else if (elevator.getDirection() == ElevatorDirection.DOWN ||
                elevator.getDirection() == ElevatorDirection.NONE && floor.getDirection() == ElevatorDirection.DOWN) {
            List<Passenger> passengersDown = floor.getPassengersDown();
            while (!passengersDown.isEmpty() && elevator.getPassengersCount() < elevator.getCapacity()) {
                elevator.getPassengers().add(passengersDown.remove(0));
            }
        }

        elevator.updateDestinationFloors();

        if (floor.getPassengersCount() == 0) {
            callsQuery.remove(floor);
            floor.setDirection(ElevatorDirection.NONE);
        }
    }

    private boolean processCall(int floorNumber, ElevatorDirection direction) {
        if (!active) {
            Floor destFloor = getRandomDestFloor(floorNumber, direction);
            if (destFloor != null) {
                Passenger passenger = new Passenger(destFloor);
                if (direction == ElevatorDirection.UP)
                    house.getFloor(floorNumber).getPassengersUp().add(passenger);
                else
                    house.getFloor(floorNumber).getPassengersDown().add(passenger);
                Floor callFloor = house.getFloor(floorNumber);
                if (callFloor.getDirection() == ElevatorDirection.NONE)
                    callFloor.setDirection(direction);
                if (!callsQuery.contains(callFloor))
                    callsQuery.add(callFloor);
                return true;
            }
        }
        return false;
    }

    private Floor getRandomDestFloor(int floorNumber, ElevatorDirection elevatorDirection) {
        Random random = new Random();
        Floor destFloor = null;
        if (elevatorDirection == ElevatorDirection.UP) {
            if (floorNumber == house.getFloorsCount())
                return null;
            int num = random.nextInt(floorNumber + 1, house.getFloorsCount() + 1);
            destFloor = house.getFloor(num);
        } else if (elevatorDirection == ElevatorDirection.DOWN) {
            if (floorNumber == 1)
                return null;
            int num = random.nextInt(1, floorNumber);
            destFloor = house.getFloor(num);
        }
        return destFloor;
    }

    private void updateElevatorBox(int up, int down) {
        Label upLabelIcon = (Label) elevatorBox.lookup("#elevator-upLabelIcon");
        Label upLabel = (Label) elevatorBox.lookup("#elevator-upLabel");

        Label downLabelIcon = (Label) elevatorBox.lookup("#elevator-downLabelIcon");
        Label downLabel = (Label) elevatorBox.lookup("#elevator-downLabel");

        upLabel.setText(String.valueOf(up));
        downLabel.setText(String.valueOf(down));

        if (up == 0) {
            upLabelIcon.getStyleClass().removeAll("up-arrow-active");
            upLabelIcon.getStyleClass().add("up-arrow-passive");
        } else {
            upLabelIcon.getStyleClass().removeAll("up-arrow-passive");
            upLabelIcon.getStyleClass().add("up-arrow-active");
        }

        if (down == 0) {
            downLabelIcon.getStyleClass().remove("down-arrow-active");
            downLabelIcon.getStyleClass().add("down-arrow-passive");
        } else {
            downLabelIcon.getStyleClass().remove("down-arrow-passive");
            downLabelIcon.getStyleClass().add("down-arrow-active");
        }
    }

    public void updateControls(Floor floor) {
        Label upLabel = (Label) grid.lookup("#" + floor.getNumber() + "-upLabel");
        Label upLabelIcon = (Label) grid.lookup("#" + floor.getNumber() + "-upLabelIcon");

        Label downLabel = (Label) grid.lookup("#" + floor.getNumber() + "-downLabel");
        Label downLabelIcon = (Label) grid.lookup("#" + floor.getNumber() + "-downLabelIcon");

        upLabel.setText(String.valueOf(floor.getPassengersUp().size()));
        downLabel.setText(String.valueOf(floor.getPassengersDown().size()));

        if (floor.getPassengersUp().isEmpty()) {
            upLabelIcon.getStyleClass().remove("up-arrow-active");
            upLabelIcon.getStyleClass().add("up-arrow-passive");
        }

        if (floor.getPassengersDown().isEmpty()) {
            downLabelIcon.getStyleClass().remove("down-arrow-active");
            downLabelIcon.getStyleClass().add("down-arrow-passive");
        }
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

            Label label = new Label(String.valueOf(house.getFloorsCount() - i + 1));
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
        upLabelIcon.setId("elevator-upLabelIcon");

        Label upLabel = new Label("0");
        upLabel.setFont(new Font(15));
        upLabel.setId("elevator-upLabel");


        upBox.getChildren().add(upLabelIcon);
        upBox.getChildren().add(upLabel);

        Label downLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_DOWN.characterToString());
        downLabelIcon.getStyleClass().addAll("down-arrow-passive", "font-awesome");
        downLabelIcon.setId("elevator-downLabelIcon");

        Label downLabel = new Label("0");
        downLabel.setFont(new Font(15));
        downLabel.setId("elevator-downLabel");

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
        upLabelIcon.setId(floorNumber + "-upLabelIcon");

        Label upLabel = new Label("0");
        upLabel.setFont(new Font(15));
        upLabel.setId(floorNumber + "-upLabel");

        upLabelIcon.setOnMouseClicked(mouseEvent -> {
            if (processCall(floorNumber, ElevatorDirection.UP)) {
                upLabelIcon.getStyleClass().add("up-arrow-active");
                int up = Integer.parseInt(upLabel.getText());
                upLabel.setText(String.valueOf(up + 1));
            }
        });

        Label downLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_DOWN.characterToString());
        downLabelIcon.getStyleClass().addAll("down-arrow-passive", "font-awesome", "control-button");
        downLabelIcon.setId(floorNumber + "-downLabelIcon");

        Label downLabel = new Label("0");
        downLabel.setFont(new Font(15));
        downLabel.setId(floorNumber + "-downLabel");

        downLabelIcon.setOnMouseClicked(mouseEvent -> {
            if (processCall(floorNumber, ElevatorDirection.DOWN)) {
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