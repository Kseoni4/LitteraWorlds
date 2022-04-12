package org.litteraworlds.game;

import org.litteraworlds.GameLoop;
import org.litteraworlds.map.*;

import java.beans.Encoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Base64;

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

    private static final SecureRandom random = new SecureRandom();

    private static final String[] zoneNames = {"Frontier", "Old lands", "Planes", "Valley", "Dead lands", "Land of the lost words"};

    public static Region generateNewRegion() {
        try {
            MessageDigest hashGen = MessageDigest.getInstance("MD5");

            byte[] hash = hashGen.digest(GameLoop.getPlayer().toString().getBytes(StandardCharsets.UTF_8));

            String hashString = Base64.getEncoder().encodeToString(hash).toUpperCase();

            System.out.println("Seed: " + hashString);

            random.setSeed(hash);

            return regionBuilder(hash);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return regionBuilder(random.generateSeed(8));
    }

    private static Region regionBuilder(byte[] seed){
        Region rgn = new Region("First", Base64.getEncoder().encodeToString(seed));
        for(int i = 0; i < 8; i++) {
            String name = zoneNames[random.nextInt(zoneNames.length)];
            rgn.addZone(new Zone(
                    name,
                    Base64.getEncoder().encodeToString(seed).concat("|"+Base64.getEncoder().encodeToString(random.generateSeed(12))),
                    new Position(Direction.values()[random.nextInt(Direction.values().length)]),
                    random.nextInt(10, 24))
            );
            random.setSeed(seed);
        }

        int i = 0;
        for(Zone zone : rgn.getZones()){
            zone.putBuildingInZone(Building.createNewBuilding(3, zone, i++));
        }

        return rgn;
    }
}
