package org.litteraworlds.game;

import org.litteraworlds.GameLoop;
import org.litteraworlds.map.*;
import org.litteraworlds.view.Debug;

import java.beans.Encoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * <h2>[CLIENT/SERVER-SIDE]</h2>
 * <h3>MapGeneration</h3>
 * <p>
 *     Класс для процедурной генерации карты мира.
 *     Карта мира имеет следующую иерархию (от крупного к мелкому):<br>
 *     Регион (класс {@link Region}) -> Зона (класс {@link Zone}) -> Здание (класс {@link Building}) -> Комната (класс {@link Room}).
 *     Игрок перемещается в зонах и в комнатах, а регион и здание являются в некотором роде "контейнерами" для хранения доступных локаций.
 * </p>
 */
public class MapGeneration {
    private MapGeneration(){}

    private static SecureRandom random;

    private static final String[] zoneNames = {"Frontier", "Old lands", "Planes",
            "Valley", "Dead lands", "Land of the lost words",
            "New Narratown", "Wordplays", "Scrap word",
            "Slurs", "Uppercases", "Lowcases"
    };

    private static String convertHashToString(byte[] hash){
        String temp = "";
        for(byte b : hash){
            temp = temp.concat(String.format("%02x",b));
        }
        return temp;
    }

    public static Region generateNewRegion(byte[] incomingHash) throws NoSuchAlgorithmException {
            Debug.toLog("Seed: " + convertHashToString(incomingHash));

            random = SecureRandom.getInstance("SHA1PRNG");

            random.setSeed(incomingHash);

            return regionBuilder(incomingHash);
    }

    private static Region regionBuilder(byte[] seed){
        Region rgn = new Region("First", convertHashToString(seed));
        for(Direction origin : Direction.values()){
            rgn.putZone(new Zone(zoneNames[random.nextInt(zoneNames.length)], origin, convertHashToString(seed)));
        }

        int i = 0;
        for(Zone zone : rgn.getZones()){
            zone.putBuildingInZone(Building.createNewBuilding(3, zone, i++));
        }

        return rgn;
    }
}
