package org.deadog.oop.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.deadog.oop.entities.Elevator;
import org.deadog.oop.entities.Floor;
import org.deadog.oop.entities.House;
import org.deadog.oop.panels.ElevatorPanel;
import org.deadog.oop.utils.ElevatorDirection;

public class ElevatorController implements ElevatorControl{
    private Elevator elevator;

    private ElevatorPanel elevatorPanel;

    public ElevatorController() {
        this.elevator = new Elevator(new Floor(1));
    }

    @Override
    public void start(ElevatorPanel elevatorPanel) {

    }

    @Override
    public void processCall(Floor floor, ElevatorDirection direction) {

    }
}