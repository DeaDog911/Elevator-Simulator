package org.deadog.oop.entities;

import org.deadog.oop.utils.ElevatorDirection;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Floor {
    private int number;

    private ElevatorDirection direction = ElevatorDirection.NONE;

    private List<Passenger> passengersUp;

    private List<Passenger> passengersDown;

    public Floor(int number) {
        this.number = number;
        passengersUp = new LinkedList<>();
        passengersDown = new LinkedList<>();
    }

    public int getNumber() {
        return number;
    }

    public ElevatorDirection getDirection() {
        return direction;
    }

    public void setDirection(ElevatorDirection direction) {
        this.direction = direction;
    }

    public List<Passenger> getPassengersUp() {
        return passengersUp;
    }

    public List<Passenger> getPassengersDown() {
        return passengersDown;
    }

    public int getPassengersCount() {
        return passengersUp.size() + passengersDown.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return number == floor.number;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }
}
