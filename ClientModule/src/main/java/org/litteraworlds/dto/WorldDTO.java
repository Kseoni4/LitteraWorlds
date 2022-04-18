package org.litteraworlds.dto;

import org.litteraworlds.map.Region;

import java.io.Serializable;
import java.util.List;

public class WorldDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String worldHashID;

    private byte[] worldHashIDBytes;

    private List<Region> worldRegions;

    public WorldDTO(String worldHashID, byte[] worldHashIDBytes, List<Region> worldRegions) {
        this.worldHashID = worldHashID;
        this.worldHashIDBytes = worldHashIDBytes;
        this.worldRegions = worldRegions;
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
