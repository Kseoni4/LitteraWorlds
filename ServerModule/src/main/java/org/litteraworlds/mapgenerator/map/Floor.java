package org.litteraworlds.mapgenerator.map;

import java.util.LinkedList;
import java.util.List;

public class Floor extends Place {

    private final int floorNumber;

    private final LinkedList<Room> rooms = new LinkedList<>();

    private final Building linkedBuilding;

    public void addRoom(Room room){
        this.rooms.addLast(room);
    }

    private void generateRooms(int count){
        for (int i = 0; i < count; i++) {
            rooms.add(i, new Room(this.getPlaceHashIDBytes(), this, i));
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
        System.out.println(rooms.toString());
        return rooms.stream().toList();
    }

    public Floor(byte[] buildingHashCode, Building linkedBuilding, int floorNumber) {
        super("Этаж "+floorNumber,Direction.NORTH, buildingHashCode);
        this.floorNumber = floorNumber;
        this.linkedBuilding = linkedBuilding;
        generateRooms(2);
        System.out.println("The floor "+floorNumber+" with hash "+this.getPlaceHashID()+" in building "+linkedBuilding + " and with "
                +this.rooms.size()+ " rooms, has created");
    }

    @Override
    public String toString(){
        return "Этаж "+floorNumber;
    }
}
