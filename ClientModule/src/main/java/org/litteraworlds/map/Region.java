package org.litteraworlds.map;
import org.litteraworlds.objects.Player;

import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Region implements Serializable {

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

    public void putPlayerIntoRandomZone(Player player) {
        int randomIndex = ThreadLocalRandom.current().nextInt(zones.size());
        player.putIntoMap(zones.get(randomIndex));
    }

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
