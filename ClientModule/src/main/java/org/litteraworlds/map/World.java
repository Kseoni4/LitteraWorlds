package org.litteraworlds.map;

import org.litteraworlds.dto.WorldDTO;
import org.litteraworlds.view.Debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World implements Serializable {
    private String worldHashID = "";

    private byte[] worldHashIDBytes;

    private byte[] worldHashSum;

    private ArrayList<Region> worldRegions = new ArrayList<>();

    public void setWorldHashSum(byte[] worldHashSum) {
        this.worldHashSum = worldHashSum;
    }

    public World(byte[] worldHashIDBytes){
        this.worldHashIDBytes = worldHashIDBytes;

        for(byte b : worldHashIDBytes){
            worldHashID = worldHashID.concat(String.format("%02x",b));
        }

        Debug.toLog("Байты: "+ Arrays.toString(worldHashIDBytes));

        Debug.toLog("Создан мир с хэшем: "+worldHashID);
    }

    public World(WorldDTO worldDTO){
        this.worldHashIDBytes = worldDTO.getWorldHashIDBytes();
        this.worldHashID = worldDTO.getWorldHashID();
        this.worldRegions = (ArrayList<Region>) worldDTO.getWorldRegions();
        this.worldHashSum = worldDTO.getWorldHashSum();
    }

    public String getWorldHashID() {
        return worldHashID;
    }

    public byte[] getWorldHashIDBytes() {
        return worldHashIDBytes;
    }

    public byte[] getWorldHashSum() {
        return worldHashSum;
    }


    public void putRegionInWorld(Region region){
        worldRegions.add(region);
    }

    public List<Region> getRegions(){
        return worldRegions;
    }
}
