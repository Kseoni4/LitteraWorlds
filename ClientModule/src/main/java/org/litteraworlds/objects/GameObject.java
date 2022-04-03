package org.litteraworlds.objects;

import org.litteraworlds.map.Place;
import org.litteraworlds.map.Position;
import org.litteraworlds.view.colors.Colors;

public abstract class GameObject {

    private static final String DEFAULT_NAME = "unnamed_object";

    private static final String DEFAULT_COLOR = Colors.GREY;

    private final int id;

    private static int idCounter = 0;

    private final String name;

    private final String color;

    private Place objectPlace;

    private Position objectPosition;

    public GameObject(String color, String name){
        this.id = ++idCounter;
        this.color = color;
        this.name = name.equals(DEFAULT_NAME) ? DEFAULT_NAME+":"+id : name;
    }

    public GameObject(String name){
        this(DEFAULT_COLOR, name);
    }

    public GameObject(){
        this(DEFAULT_COLOR, DEFAULT_NAME);
    }

    public void putIntoMap(Place map){
        this.objectPlace = map;
        this.objectPosition = map.getPlacePosition();
        map.objectsInPlace.add(this);
    }

    public void placeIntoPosition(Position position){
        this.objectPosition = position;
    }

    @Override
    public String toString(){
        return name+Colors.R;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Place getObjectPlace() {
        return objectPlace;
    }

    public Position getObjectPosition() {
        return objectPosition;
    }
}
