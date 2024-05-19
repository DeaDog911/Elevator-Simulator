package org.deadog.oop.entities;

import org.deadog.oop.utils.ElevatorDirection;

import javax.print.attribute.standard.Destination;
import java.util.*;

public class Elevator {
    private Floor currentFloor;

    private int capacity = 4;

    private ElevatorDirection direction = ElevatorDirection.NONE;

    private List<Passenger> passengers;

    private List<Floor> destinationFloors;

    private int travelTime = 5  ;

    public Elevator(Floor currentFloor) {
        this.currentFloor = currentFloor;
        this.passengers = new LinkedList<>();
        this.destinationFloors = new LinkedList<>();
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

    public ElevatorDirection getDirection() {
        return direction;
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Floor> getDestinationFloors() {
        return destinationFloors;
    }

    public void removePassengers(Floor floor) {
        passengers.removeIf(passenger -> passenger.getDestinationFloor().equals(floor));
    }

    public void updateDestinationFloors() {
        destinationFloors.clear();
        for (Passenger passenger : passengers) {
            if (!destinationFloors.contains(passenger.getDestinationFloor())) {
                destinationFloors.add(passenger.getDestinationFloor());
            }
        }

        // Сортируем этажи назначения в зависимости от направления движения лифта
        if (direction == ElevatorDirection.UP) {
            destinationFloors.sort(Comparator.comparingInt(Floor::getNumber));
        } else if (direction == ElevatorDirection.DOWN) {
            destinationFloors.sort(Comparator.comparingInt(Floor::getNumber).reversed());
        }
    }

    public int getPassengersCountUp() {
        return (int) passengers.stream().filter(x -> x.getDestinationFloor().getNumber() > currentFloor.getNumber()).count();
    }

    public int getPassengersCountDown() {
        return (int) passengers.stream().filter(x -> x.getDestinationFloor().getNumber() < currentFloor.getNumber()).count();
    }
}
