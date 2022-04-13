package org.litteraworlds.map;
import org.litteraworlds.map.Direction;
import org.litteraworlds.net.Requests;
import org.litteraworlds.objects.GameObject;
import org.litteraworlds.view.Debug;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Place implements Serializable {
    private String placeHashID;

    private ArrayList<String> aroundPlacesID;

    private String placeName;

    private LocalDateTime createTime;

    private Direction originFromPivot;

    public ArrayList<GameObject> objectsInPlace;

    public Place(Direction originFromPivot, String hash){
        this("", originFromPivot, hash);
    }

    public Place(String name, Direction originFromPivot, String hash){
        this.aroundPlacesID = new ArrayList<>();
        this.createTime = LocalDateTime.now();
        this.placeName = name;
        this.originFromPivot = originFromPivot;
        this.objectsInPlace = new ArrayList<>();
        String dataToHash = this.placeName+this.originFromPivot+hash;

        Debug.toLog("Data to hash: "+dataToHash);

        placeHashID = convertHashToString(Requests.getHash());
    }

    private static String convertHashToString(byte[] hash){
        String temp = "";
        for(byte b : hash){
            temp = temp.concat(String.format("%02x",b));
        }
        return temp;
    }


    public GameObject findObjectInPlace(String name){
        Debug.toLog(Arrays.toString(objectsInPlace.toArray()));
        Optional<GameObject> optionalGameObject = objectsInPlace.stream()
                .filter(gameObject -> gameObject.getName().equals(name))
                .findFirst();
        return optionalGameObject.or(()-> {
            Debug.toLog("Object "+name+" is not found");
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

    public ArrayList<String> getAroundPlacesID() {
        return aroundPlacesID;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + ":" + getPlaceName() + ", сторона " + getOriginFromPivot().toString();
    }
}