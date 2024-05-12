package org.deadog.oop.controllers;

import org.deadog.oop.entities.Floor;
import org.deadog.oop.panels.ElevatorPanel;
import org.deadog.oop.utils.ElevatorDirection;

public interface ElevatorControl {
    void start(ElevatorPanel elevatorPanel);

    void processCall(Floor floor, ElevatorDirection direction);
}
