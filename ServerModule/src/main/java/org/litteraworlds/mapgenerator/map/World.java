package org.litteraworlds.mapgenerator.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {
    private String worldHashID = "";

    private byte[] worldHashIDBytes;

    private ArrayList<Region> worldRegions = new ArrayList<>();

    public World(byte[] worldHashIDBytes){
        this.worldHashIDBytes = worldHashIDBytes;

        for(byte b : worldHashIDBytes){
            worldHashID = worldHashID.concat(String.format("%02x",b));
        }

        System.out.println("Создан мир с хэшем: "+worldHashID);
    }

    public String getWorldHashID() {
        return worldHashID;
    }

    public byte[] getWorldHashIDBytes() {
        return worldHashIDBytes;
    }

    public void putRegionInWorld(Region region){
        worldRegions.add(region);
    }

    public List<Region> getRegions(){
        return worldRegions;
    }
}
