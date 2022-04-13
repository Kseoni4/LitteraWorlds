package org.litteraworlds.mapgenerator.map;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Region {

    private String regionHashID;

    private String regionName;

    private ArrayList<Zone> zones = new ArrayList<>();

    public Region(){
        this("unknownRegion","");
    }

    public Region(String regionName, String regionHashID){
        this.regionName = regionName;

        this.regionHashID = regionHashID;
    }

/*    public void putPlayerIntoRandomZone(Player player) {
        int randomIndex = ThreadLocalRandom.current().nextInt(zones.size());
        player.putIntoMap(zones.get(randomIndex));
    }*/

    public List<Zone> getZones() {
        return zones;
    }

    public String getRegionHashID() {
        return regionHashID;
    }

    public String getRegionName() {
        return regionName;
    }

    public void putZone(Zone zone){
        this.zones.add(zone);
    }

    @Override
    public String toString() {
        return "Region:" + this.regionName;
    }
}
