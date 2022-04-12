package org.litteraworlds.map;

public class Zone extends Place{

    public Zone(String hashId) {
        this("unknown", hashId);
    }

    public Zone(String name, String hashId) {
        super(name, hashId);
    }

    public Zone(String name, Position zonePosition, String hashId){
        super(name, zonePosition, hashId);
    }
}
