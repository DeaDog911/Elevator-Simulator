package org.deadog.oop.entities;

public class House {
    private int entrancesCount;

    private int floorsCount;

    public House(int entrancesCount, int floorsCount) {
        this.entrancesCount = entrancesCount;
        this.floorsCount = floorsCount;
    }

    public int getEntrancesCount() {
        return entrancesCount;
    }

    public int getFloorsCount() {
        return floorsCount;
    }
}
