package org.litteraworlds.map;

import org.litteraworlds.objects.GameObject;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Place {

    private String name;

    private String hashID;

    private Position placePosition;

    public ArrayList<GameObject> objectsInPlace;

    protected Place(String name, String hashCode, Position placePosition){
        this.name = name;
        this.hashID = hashCode;
        this.placePosition = placePosition;
        this.objectsInPlace = new ArrayList<>();
    }

    public GameObject findObjectInPlace(String name){
        Optional<GameObject> optionalGameObject = objectsInPlace.stream()
                .filter(gameObject -> gameObject.getName().equals(name))
                .findFirst();
        return optionalGameObject.orElseThrow();
    }

    public GameObject findObjectInPlace(String name, int id){
        Optional<GameObject> optionalGameObject = objectsInPlace.stream()
                .filter(gameObject -> (gameObject.getName().equals(name) && gameObject.getID() == id))
                .findFirst();
        return optionalGameObject.orElseThrow();
    }

    public GameObject findObjectInPlace(GameObject object){
        Optional<GameObject> optionalGameObject = objectsInPlace.stream()
                .filter(gameObject -> gameObject.equals(object))
                .findFirst();
        return optionalGameObject.orElseThrow();
    }

    protected Place(String hashCode, Position placePosition){
        this("", hashCode, placePosition);
    }

    protected Place(String hashCode){
        this("",hashCode,new Position(0,0));
    }

    public Position getPlacePosition() {
        return placePosition;
    }

    public String getName() {
        return name;
    }

    public String getHashCode() {
        return hashID;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + ":" + getName() + " сторона " + getPlacePosition().toString();
    }
}
