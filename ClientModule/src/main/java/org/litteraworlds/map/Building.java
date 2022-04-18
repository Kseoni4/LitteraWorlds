package org.litteraworlds.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Building extends Place {

    private final Zone linkedZone;

    private final LinkedList<Floor> floors = new LinkedList<>();

    private Building(int floorsCount, Direction originFromZoneCenter, Zone linkedZone, byte[] zoneHash){
        super("Здание в "+linkedZone.toString(), originFromZoneCenter, zoneHash);
        this.linkedZone = linkedZone;
        generateFloors(floorsCount);
    }

    private void generateFloors(int count) {
        System.out.println(this+" generate floors");
        for(int i = 0; i < count; i++){
            System.out.println("Add floor "+i);
            floors.add(i, new Floor(linkedZone.getPlaceHashIDBytes(),this, i));
        }
    }

    public Floor getFirstFloor(){
        System.out.println(floors.toString());
        return floors.getFirst();
    }

    public List<Floor> getFloors(){
        return floors;
    }

    public List<Room> getRoomsFromFloor(int floorIndex){

        return floors.get(floorIndex).getRoomsOnFloor();
    }

    public List<Room> getRoomList(){
        List<Room> rooms = new ArrayList<>();
        for(Floor floor : floors){
            rooms.addAll(floor.getRoomsOnFloor());
        }
        return rooms;
    }

    public static Building createNewBuilding(int roomsCount, Direction originFromZoneCenter, Zone linkedZone, byte[] zoneHash){
        return new Building(roomsCount, originFromZoneCenter, linkedZone, zoneHash);
    }

    public Zone getLinkedZone(){
        return this.linkedZone;
    }

    private String getBuildingDesc(){
        return "Здание высотой "+ floors.size() + (floors.size() > 4 ? " этажей" : (floors.size() > 1 ? " этажа" : " этаж"));
    }

    @Override
    public String toString(){
        return getBuildingDesc();
    }

}
