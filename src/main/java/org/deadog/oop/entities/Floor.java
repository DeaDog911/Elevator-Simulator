package org.deadog.oop.entities;

import org.deadog.oop.utils.ElevatorDirection;

import java.util.LinkedList;
import java.util.List;

public class Floor {
    private int number;

    private ElevatorDirection callStatus = ElevatorDirection.NONE;

    private List<Passenger> passengerList;

    public Floor(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ElevatorDirection getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(ElevatorDirection callStatus) {
        this.callStatus = callStatus;
    }

    public List<Passenger> getPassengerList() {
        if (passengerList == null)
            passengerList = new LinkedList<>();
        return passengerList;
    }
}
