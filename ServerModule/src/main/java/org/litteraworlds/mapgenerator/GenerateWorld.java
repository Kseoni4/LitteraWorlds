package org.litteraworlds.mapgenerator;

import org.litteraworlds.mapgenerator.map.*;
import org.litteraworlds.security.HashGen;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
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

        String hashLine = convertHashToString(hash);

        gwLog.info("Seed: " + hashLine);

        random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(hash);

        gwLog.info("Генерация мира");

        World world = new World(hash);

        gwLog.info("Генерация региона на основе World Hash");

        Region region = new Region("Genesis Region", world.getWorldHashIDBytes());

        world.putRegionInWorld(region);

        ArrayList<Integer> numbers = new ArrayList<>();

        int nameIndex = random.nextInt(zoneNames.length);

        int originIndex = random.nextInt(Direction.values().length);

        int roomsCount = random.nextInt(11);

        numbers.add(nameIndex);

        numbers.add(originIndex);

        numbers.add(roomsCount);

        region.putZone(new Zone(zoneNames[nameIndex], Direction.values()[originIndex], region.getRegionHashIDBytes()));

        Zone zone = region.getZone(zoneNames[nameIndex]);

        int originBuildingRandom = random.nextInt(Direction.values().length);

        zone.putBuildingInZone(Building.createNewBuilding(roomsCount, Direction.values()[originBuildingRandom], zone, zone.getPlaceHashIDBytes()));

        System.out.println("===============ИНФОРМАЦИЯ О ГЕНЕРАЦИИ====================");
        System.out.println("Вводная строка: "+data);
        System.out.println("Полученный хэш: "+hashLine);
        System.out.println("Полученный хэш (байты): "+Arrays.toString(hash));
        //Набор случ.чисел:[9, 10, 4, 10, 8, 11, 1, 9]
        System.out.println("Набор случ.чисел:"+ numbers);
        System.out.println(region);
        for(Zone zone1 : region.getZones()) {
            System.out.println(zone1);
            for(Building building : zone1.getBuildingsInZone()){
                System.out.println(building);
                for(Room room : building.getRoomList()){
                    System.out.println(room);
                }
            }
        }

        System.out.println("Цепочка хэшей:");
        System.out.println("World:"+world.getWorldHashID());
        for (Region rg : world.getRegions()){
            System.out.println(rg.toString()+":"+rg.getRegionHashID());
            for(Zone zn : rg.getZones()){
                System.out.println(zn.toString() + ":"+zn.getPlaceHashID());
                for (Building bg : zn.getBuildingsInZone()){
                    System.out.println(bg.toString());
                    for(Floor floor : bg.getFloors()){
                        System.out.println(floor.toString()+":"+floor.getPlaceHashID());
                        for(Room rm : floor.getRoomsOnFloor()){
                            System.out.println(rm.toString()+":"+rm.getPlaceHashID());
                        }
                    }
                }
            }
        }

        System.out.println("Перемножение хешей");
        System.out.println("World hash line"+world.getWorldHashID());
        System.out.println("World hash bytes"+ Arrays.toString(world.getWorldHashIDBytes()));

        System.out.println("Region hash line: "+hashLine);
        System.out.println("Region hash bytes: "+ Arrays.toString(region.getRegionHashIDBytes()));

        byte[] multiply = world.getWorldHashIDBytes();


        for(int i = 0; i < multiply.length; i++){
            multiply[i] += region.getRegionHashIDBytes()[i];
        }

        System.out.println(Arrays.toString(region.getZones().get(0).getPlaceHashIDBytes()));
        for(int i = 0; i < multiply.length;i++){
            multiply[i] += region.getZones().get(0).getPlaceHashIDBytes()[i];
        }

        System.out.println(Arrays.toString(region.getZones().get(0).getBuildingsInZone().get(0).getPlaceHashIDBytes()));
        for(int i = 0; i < multiply.length;i++){
            multiply[i] += region.getZones().get(0).getBuildingsInZone().get(0).getPlaceHashIDBytes()[i];
        }


        for(Floor floor : region.getZones().get(0).getBuildingsInZone().get(0).getFloors()) {
            for(Room room : floor.getRoomsOnFloor()){
                for(int i = 0; i < multiply.length;i++){
                    multiply[i] += floor.getPlaceHashIDBytes()[i];
                    multiply[i] += room.getPlaceHashIDBytes()[i];
                }
            }
        }



        System.out.println("Result in bytes: "+Arrays.toString(multiply));

        System.out.println("Result in string:"+convertHashToString(multiply));

        System.out.println("===================================");
    }
}
