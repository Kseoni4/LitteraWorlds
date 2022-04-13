package org.litteraworlds.mapgenerator.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Zone extends Place {

    private ArrayList<Building> buildingsInZone;

    private LinkedList<Zone> connectedZones;

    private int zoneSquare;

    public Zone(Direction originFromRegionCenter, String hashId){
        this("unknownZone", originFromRegionCenter, hashId);
    }

    public Zone(String name, Direction originFromRegionCenter, String hashId){
        super(name, originFromRegionCenter, hashId);
        buildingsInZone = new ArrayList<>();
    }

    public void connectZone(Zone zone){
        connectedZones.addLast(zone);
    }

    public void putBuildingInZone(Building building){
        this.buildingsInZone.add(building);
        building.putIntoMap(this);
    }

    public List<Building> getBuildingsInZone(){
        return this.buildingsInZone;
    }

    public Building getBuilding(int buildingID){
        Optional<Building> optionalBuilding = buildingsInZone.stream().filter(building -> building.getBuildingID() == buildingID).findFirst();
        return optionalBuilding.orElseThrow();
    }
    @Override
    public String toString(){
        return super.toString() + " текущего региона";
    }
}
