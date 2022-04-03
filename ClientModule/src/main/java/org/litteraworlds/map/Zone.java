package org.litteraworlds.map;

import org.litteraworlds.objects.GameObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

public class Zone extends Place{

    private ArrayList<Building> buildingsInZone;

    private LinkedList<Zone> connectedZones;

    private int zoneSquare;

    public Zone(String name, String hashID, Position zonePositionInRegion, int zoneSquare) {
        super(name, hashID, zonePositionInRegion);
        this.zoneSquare = zoneSquare;
        this.connectedZones = new LinkedList<>();
        this.buildingsInZone = new ArrayList<>();
    }

    public void connectZone(Zone zone){
        connectedZones.addLast(zone);
    }

    public void putBuildingInZone(Building building){
        this.buildingsInZone.add(building);
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
