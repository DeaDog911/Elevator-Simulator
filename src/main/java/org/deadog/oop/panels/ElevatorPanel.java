package org.deadog.oop.panels;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.deadog.oop.controllers.ElevatorControl;
import org.deadog.oop.controllers.ElevatorController;
import org.deadog.oop.entities.Floor;
import org.deadog.oop.entities.House;
import org.deadog.oop.utils.ElevatorDirection;

public class ElevatorPanel extends VBox {
    private GridPane grid;

    private VBox elevator;

    public final int FLOORS_COUNT;

    private ElevatorControl elevatorControl;

    public ElevatorPanel(int floorsCount, double width, double height, ElevatorControl elevatorControl) {
        FLOORS_COUNT = floorsCount;
        this.elevatorControl = elevatorControl;

        this.setPrefSize(width, height);
        this.setAlignment(Pos.CENTER);

        grid = initializeGrid();
        elevator = initializeElevator();
        Button launchButton = new Button("Запуск");
        launchButton.setOnMouseClicked(e -> {
            elevatorControl.start(this);
        });

        grid.add(elevator, 1, FLOORS_COUNT);
        grid.setPadding(new Insets(0, 0, 20, 0));

        this.getChildren().add(grid);
        this.getChildren().add(launchButton);
    }

    public void moveToFloor(Floor floor) {
        grid.getChildren().remove(elevator);
        grid.add(elevator, 1, FLOORS_COUNT-floor.getNumber()+1);
    }

    private GridPane initializeGrid() {
        GridPane grid = new GridPane();
        for (int i = 0; i < 3; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints());
        }
        grid.setPrefWidth(this.getPrefWidth());
        grid.setPrefHeight(this.getPrefHeight());
        for (int i = 1; i <= FLOORS_COUNT; i++) {
            grid.getRowConstraints().add(new RowConstraints());

            Label label = new Label(String.valueOf(10-i));
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setPrefWidth(100);
            label.setPrefHeight(70);
            label.setFont(new Font(25));

            grid.add(label, 0, i);

            HBox controlBox = initializeControlBox(i);
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
            upLabelIcon.getStyleClass().add("up-arrow-active");
            int up = Integer.parseInt(upLabel.getText());
            upLabel.setText(String.valueOf(up+1));
            elevatorControl.processCall(new Floor(floorNumber), ElevatorDirection.UP);
        });

        Label downLabelIcon = new Label(FontAwesomeIcon.ARROW_CIRCLE_DOWN.characterToString());
        downLabelIcon.getStyleClass().addAll("down-arrow-passive", "font-awesome", "control-button");

        Label downLabel = new Label("0");
        downLabel.setFont(new Font(15));

        downLabelIcon.setOnMouseClicked(mouseEvent -> {
            int down = Integer.parseInt(downLabel.getText());
            downLabel.setText(String.valueOf(down + 1));
            downLabelIcon.getStyleClass().add("down-arrow-active");
            elevatorControl.processCall(new Floor(floorNumber), ElevatorDirection.DOWN);
        });

        hBox.getChildren().add(upLabelIcon);
        hBox.getChildren().add(upLabel);
        hBox.getChildren().add(downLabelIcon);
        hBox.getChildren().add(downLabel);
        hBox.setPrefHeight(70);

        return hBox;
    }
}
