package org.litteraworlds.map;

import org.litteraworlds.security.HashGen;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class Region {

    private String regionHashID;

    private String regionName;

    private ArrayList<Zone> zones = new ArrayList<>();

    public Region(){
        regionName = "Genesis planes";

        String dataToHash = regionName.concat(" Create time ").concat(LocalDateTime.now().toString());

        Logger.getGlobal().info("Data region to hash: "+dataToHash);

        regionHashID = HashGen.getHash(dataToHash);

        try {
            generateZones();
        } catch (Exception e){
            Logger.getGlobal().info(e.toString());
        }
    }

    private void generateZones() throws Exception {
        Random random = SecureRandom.getInstanceStrong();
        for(Direction direction : Direction.values()){
            String hash;
            if(!zones.isEmpty()){
                hash = zones.get(zones.size()-1).getPlaceHashID();
            } else {
                hash = regionHashID;
            }
            zones.add(new Zone(
                            direction.toString(),
                            new Position(random.nextInt(6), random.nextInt(7))
                            ,hash
                    )
            );
        }
        for(int i = 0; i < zones.size()-1; i+=2){
            Logger.getGlobal().info("Z2"
                    +zones.get(i+1).getPlacePosition()
                    +" находится в стороне "
                    +Direction.calculateNextPlaceDirection(zones.get(i).getPlacePosition(), zones.get(i+1).getPlacePosition())
                    + " от Z1"
                    +zones.get(i).getPlacePosition()
            );
        }
    }

}
