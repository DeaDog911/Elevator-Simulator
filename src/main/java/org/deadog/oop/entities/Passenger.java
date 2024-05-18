package org.deadog.oop.entities;

public class Passenger {
    private Floor destinationFloor;

    public Passenger(Floor destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public Floor getDestinationFloor() {
        return destinationFloor;
    }
}
