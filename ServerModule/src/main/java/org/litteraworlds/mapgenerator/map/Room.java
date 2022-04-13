package org.litteraworlds.mapgenerator.map;

public class Room extends Place {

    private int roomIndex;

    private Floor floorInBuilding;

    private Room nextRoom;

    private Room backRoom = null;

    public Room(String hashCode, Floor floorInBuilding, int roomIndex) {
        super("", Direction.NORTH, hashCode);
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

    @Override
    public String toString(){
        return "Комната "+roomIndex+" этаж "+floorInBuilding.getFloorNumber();
    }
}
