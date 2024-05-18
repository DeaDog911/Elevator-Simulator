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

    private int travelTime = 1;

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

    public int getPassengersCount() {
        return passengers.size();
    }

    public List<Floor> getDestinationFloors() {
        return destinationFloors;
    }

    public int removePassengers(Floor floor) {
        int count = 0;
        Iterator<Passenger> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getDestinationFloor().equals(floor)) {
                iterator.remove();
                count++;
            }
        }
        return count;
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
            Collections.sort(destinationFloors, Comparator.comparingInt(Floor::getNumber));
        } else if (direction == ElevatorDirection.DOWN) {
            Collections.sort(destinationFloors, Comparator.comparingInt(Floor::getNumber).reversed());
        }
    }

    public int getPassengersCountUp() {
        return (int) passengers.stream().filter(x -> x.getDestinationFloor().getNumber() > currentFloor.getNumber()).count();
    }

    public int getPassengersCountDown() {
        return (int) passengers.stream().filter(x -> x.getDestinationFloor().getNumber() < currentFloor.getNumber()).count();
    }
}
