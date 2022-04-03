package org.litteraworlds.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Building {

    private final int buildingID;

    private final Zone linkedZone;

    private LinkedList<Room> rooms = new LinkedList<>();

    private Building(int roomsCount, Zone linkedZone, int id){
        this.linkedZone = linkedZone;
        this.buildingID = id;
        generateRooms(roomsCount);
    }

    private void generateRooms(int count) {
        for (int i = 0; i < count; i++) {
            rooms.add(i, new Room(linkedZone.getHashCode().concat(""+(buildingID*(i+1)*2)), this, i));
            if (i > 0) {
                rooms.get(i).setBackRoom(rooms.get(i - 1));
                rooms.get(i - 1).setNextRoom(rooms.getLast());
            }
        }
    }

    public Room getRoom(int roomIndex){
        return rooms.get(roomIndex);
    }

    public List<Room> getRoomList(){
        return rooms;
    }

    public int getBuildingID(){
        return buildingID;
    }

    public static Building createNewBuilding(int roomsCount, Zone linkedZone, int id){
        return new Building(roomsCount, linkedZone, id);
    }

}
