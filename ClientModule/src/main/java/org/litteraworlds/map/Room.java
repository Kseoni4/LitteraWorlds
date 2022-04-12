package org.litteraworlds.map;

import org.litteraworlds.objects.GameObject;

import java.util.ArrayList;

public class Room extends Place {

    private int roomIndex;

    private Floor floorInBuilding;

    private Room nextRoom;

    private Room backRoom = null;

    public Room(String hashCode, Floor floorInBuilding, int roomIndex) {
        super(hashCode);
        this.floorInBuilding = floorInBuilding;
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
