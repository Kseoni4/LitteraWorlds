package org.litteraworlds.mapgenerator.map;

import org.litteraworlds.security.HashGen;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Place implements Serializable {
    private String placeHashID;

    private byte[] placeHashIDBytes;

    private ArrayList<String> aroundPlacesID;

    private String placeName;

    private Direction originFromPivot;

    public ArrayList<GameObject> objectsInPlace;

    public Place(Direction originFromPivot, byte[] hash){
        this("", originFromPivot, hash);
    }

    public Place(String name, Direction originFromPivot, byte[] hash){
        this.aroundPlacesID = new ArrayList<>();
        this.placeName = name;
        this.originFromPivot = originFromPivot;
        this.objectsInPlace = new ArrayList<>();
        String dataToHash = this.placeName+this.originFromPivot+convertHashToString(hash);

        System.out.println("Data to hash: "+dataToHash);

        this.placeHashIDBytes = HashGen.getHash(dataToHash);

        this.placeHashID = convertHashToString(this.placeHashIDBytes);
    }

    private static String convertHashToString(byte[] hash){
        String temp = "";
        for(byte b : hash){
            temp = temp.concat(String.format("%02x",b));
        }
        return temp;
    }


    public GameObject findObjectInPlace(String name){
        System.out.println(Arrays.toString(objectsInPlace.toArray()));
        Optional<GameObject> optionalGameObject = objectsInPlace.stream()
                .filter(gameObject -> gameObject.getName().equals(name))
                .findFirst();
        return optionalGameObject.or(()-> {
            System.out.println("Object "+name+" is not found");
            return Optional.empty();
        }).orElse(null);
    }

    public GameObject findObjectInPlace(String name, int id) {
        Optional<GameObject> optionalGameObject = objectsInPlace.stream()
                .filter(gameObject -> (gameObject.getName().equals(name) && gameObject.getID() == id))
                .findFirst();
        return optionalGameObject.orElseThrow();
    }

    public List<GameObject> findObjectsInPlace(GameObject object) {
        return objectsInPlace.stream().filter(gameObject -> gameObject.getName().equals(object.getName())).collect(Collectors.toList());
    }

    public Direction getOriginFromPivot() {
        return originFromPivot;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceHashID() {
        return placeHashID;
    }

    public byte[] getPlaceHashIDBytes() {
        return placeHashIDBytes;
    }

    public ArrayList<String> getAroundPlacesID() {
        return aroundPlacesID;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + ":" + getPlaceName() + ", сторона " + getOriginFromPivot().toString();
    }
}