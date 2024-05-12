package org.deadog.oop.entities;

import org.deadog.oop.utils.ElevatorDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Elevator {
    private Floor currentFloor;

    private int capacity = 4;

    private ElevatorDirection direction = ElevatorDirection.NONE;

    private List<Passenger> passengerList;

    private int travelTime = 1;

    public Elevator(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    public List<Passenger> getPassengerList() {
        if (passengerList == null)
            passengerList = new LinkedList<>();
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }
}
