package org.deadog.oop.entities;

import java.util.ArrayList;
import java.util.List;

public class House {
    private int entrancesCount;

    private int floorsCount;

    private List<Floor> floorsList;

    public House(int entrancesCount, int floorsCount) {
        this.entrancesCount = entrancesCount;
        this.floorsCount = floorsCount;
        this.floorsList = new ArrayList<>(floorsCount);
        for (int i = 1; i <= floorsCount; i++) {
            floorsList.add(new Floor(i));
        }
    }

    public int getEntrancesCount() {
        return entrancesCount;
    }

    public int getFloorsCount() {
        return floorsCount;
    }

    public Floor getFloor(int num) {
        return floorsList.get(num-1);
    }
}
