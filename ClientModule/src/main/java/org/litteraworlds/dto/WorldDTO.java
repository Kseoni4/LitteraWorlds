package org.litteraworlds.dto;

import org.litteraworlds.map.Region;
import org.litteraworlds.map.World;

import java.io.Serializable;
import java.util.List;

public class WorldDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String worldHashID;

    private byte[] worldHashIDBytes;

    private byte[] worldHashSum;

    private List<Region> worldRegions;

    public WorldDTO(World world) {
        this.worldHashID = world.getWorldHashID();
        this.worldHashIDBytes = world.getWorldHashIDBytes();
        this.worldRegions = world.getRegions();
        this.worldHashSum = world.getWorldHashSum();
    }

    public byte[] getWorldHashSum() {
        return worldHashSum;
    }

    public String getWorldHashID() {
        return worldHashID;
    }

    public byte[] getWorldHashIDBytes() {
        return worldHashIDBytes;
    }

    public List<Region> getWorldRegions() {
        return worldRegions;
    }
}
