package org.litteraworlds.mapgenerator;

import org.litteraworlds.mapgenerator.map.*;
import org.litteraworlds.security.HashGen;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class GenerateWorld {

    private static Logger gwLog = Logger.getGlobal();

    private static final String[] zoneNames =
            {"Frontier", "Old lands", "Planes",
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

    private static SecureRandom random;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        gwLog.info("Введите строку для генерации:");
        String data = new Scanner(System.in).next();

        byte[] hash =  HashGen.getHash(data);

        gwLog.info("Seed: " + convertHashToString(hash));

        random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(hash);

        gwLog.info("Генерация региона");

        Region region = new Region("Genesis Region",convertHashToString(hash));

        ArrayList<Integer> numbers = new ArrayList<>();
        for(Direction origin : Direction.values()){
            int index = random.nextInt(zoneNames.length);
            numbers.add(index);
            region.putZone(new Zone(zoneNames[index], origin, convertHashToString(hash)));
        }

        int i = 0;
        for(Zone zone : region.getZones()){
            zone.putBuildingInZone(Building.createNewBuilding(3, zone, i++));
        }

        System.out.println("===============ИНФОРМАЦИЯ О ГЕНЕРАЦИИ====================");
        System.out.println("Вводная строка: "+data);
        System.out.println("Полученный хэш: "+convertHashToString(hash));
        System.out.println("Набор случ.чисел:"+ numbers);
        System.out.println(region);
        for(Zone zone : region.getZones()) {
            System.out.println(zone);
            for(Building building : zone.getBuildingsInZone()){
                System.out.println(building);
                for(Room room : building.getRoomList()){
                    System.out.println(room);
                }
            }
        }
        System.out.println("===================================");
    }
}
