package org.litteraworlds.map;

import org.litteraworlds.security.HashGen;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;

public abstract class Place implements Serializable {
    private String placeHashID;

    private ArrayList<String> aroundPlacesID;

    private String placeName;

    private LocalDateTime createTime;

    private Position placePosition;

    public Place(String name, Position placePosition, String hash){
        this.aroundPlacesID = new ArrayList<>();
        this.createTime = LocalDateTime.now();
        this.placeName = name;
        this.placePosition = placePosition;
        String dataToHash = this.placeName+this.placePosition+this.createTime+hash;
        Logger.getGlobal().info("Data to hash: "+dataToHash);
        placeHashID = generateHash(dataToHash);
    }

    public Place(String name, String hash){
        this(name, new Position(0,0), hash);
    }

    public Position getPlacePosition() {
        return placePosition;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceHashID() {
        return placeHashID;
    }

    public ArrayList<String> getAroundPlacesID() {
        return aroundPlacesID;
    }

    private String generateHash(String data){
        return HashGen.getHash(data);
    }
}
