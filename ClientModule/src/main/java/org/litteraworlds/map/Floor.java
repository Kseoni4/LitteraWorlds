package org.litteraworlds.map;

import org.litteraworlds.view.Debug;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

public class Floor extends Place{

    private final int floorNumber;

    private final LinkedList<Room> rooms = new LinkedList<>();

    private final Building linkedBuilding;

    public void addRoom(Room room){
        this.rooms.addLast(room);
    }

    private void generateRooms(int count){
        for (int i = 0; i < count; i++) {
            rooms.add(i, new Room(this.getHashCode().concat(""+(linkedBuilding.getBuildingID()*(i+1)*2)), this, i));
            if (i > 0) {
                rooms.get(i).setBackRoom(rooms.get(i - 1));
                rooms.get(i - 1).setNextRoom(rooms.getLast());
            }
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Room getRoom(int roomNumber){
        return this.rooms.get(roomNumber);
    }

    public List<Room> getRoomsOnFloor(){
        Debug.toLog(rooms.toString());
        return rooms.stream().toList();
    }

    public Floor(String hashCode, Building linkedBuilding, int floorNumber) {
        super(hashCode);
        this.floorNumber = floorNumber;
        this.linkedBuilding = linkedBuilding;
        generateRooms(2);
        Debug.toLog("The floor "+floorNumber+" with hash "+this.getHashCode()+" in building "+linkedBuilding + " and with "
                +this.rooms.size()+ " rooms, has created");
    }

    @Override
    public String toString(){
        return "Этаж "+floorNumber;
    }
}
