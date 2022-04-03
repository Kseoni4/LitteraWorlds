package org.litteraworlds.map;

import org.litteraworlds.objects.GameObject;

import java.util.ArrayList;

public class Room extends Place {

    private int roomIndex;

    private Building building;

    private Room nextRoom;

    private Room backRoom = null;

    public Room(String hashCode, Building linkBuilding, int roomIndex) {
        super(hashCode);
        this.building = linkBuilding;
        this.roomIndex = roomIndex;
    }

    public void placeObjectInRoom(GameObject gameObject){
        gameObject.putIntoMap(this);
    }

    public void setNextRoom(Room nextRoom) {
        this.nextRoom = nextRoom;
    }

    public void setBackRoom(Room backRoom) {
        this.backRoom = backRoom;
    }
}
