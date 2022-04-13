package org.litteraworlds.mapgenerator.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Building extends GameObject {

    private final int buildingID;

    private final Zone linkedZone;

    private final LinkedList<Floor> floors = new LinkedList<>();

    private Building(int floorsCount, Zone linkedZone, int id){
        this.linkedZone = linkedZone;
        this.buildingID = id;
        generateFloors(floorsCount);
        setName("Здание");
    }

    private void generateFloors(int count) {
        System.out.println(this+" generate floors");
        for(int i = 0; i < count; i++){
            System.out.println("Add floor "+i);
            floors.add(i, new Floor(linkedZone.getPlaceHashID().concat("|"+buildingID),this, i));
        }
    }

    public Floor getFirstFloor(){
        System.out.println(floors.toString());
        return floors.getFirst();
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

    public int getBuildingID(){
        return buildingID;
    }

    public static Building createNewBuilding(int roomsCount, Zone linkedZone, int id){
        return new Building(roomsCount, linkedZone, id);
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
