package org.litteraworlds.map;

import org.litteraworlds.objects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Region {

    private final int id;

    private String name;

    private final String REGION_HASH_CODE;

    private ArrayList<Zone> zones = new ArrayList<>();

    private int regionSize;

    public Region(String name, String REGION_HASH_CODE) {
        this.id = ThreadLocalRandom.current().nextInt(1000,9999);
        this.name = name;
        this.REGION_HASH_CODE = REGION_HASH_CODE;
    }

    public void loadZones(ArrayList<Zone> zones){
        this.zones = new ArrayList<>(zones);
    }

    public Zone getZoneByName(String zoneName){
        Optional<Zone> zoneOptional = zones.stream().filter(zone -> zone.getName().equals(zoneName)).findFirst();
        return zoneOptional.orElseThrow();
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void putPlayerIntoRandomZone(Player player){
        int randomIndex = ThreadLocalRandom.current().nextInt(zones.size());
        player.putIntoMap(zones.get(randomIndex));
    }

    public void addZone(Zone zone){
        zones.add(zone);
    }

    public String getHashCode(){
        return this.REGION_HASH_CODE;
    }

    @Override
    public String toString(){
        return "Region:"+name;
    }
}