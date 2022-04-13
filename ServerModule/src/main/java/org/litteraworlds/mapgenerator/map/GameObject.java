package org.litteraworlds.mapgenerator.map;

public abstract class GameObject {

    private static final String DEFAULT_NAME = "unnamed_object";

    //private static final String DEFAULT_COLOR = Colors.GREY;

    private final int id;

    private static int idCounter = 0;

    private String name;

    private final String color;

    private Place objectPlace;

    private Position objectPosition;

    private Direction objectOrigin;

    public GameObject(String color, String name){
        this.id = ++idCounter;
        this.color = color;
        this.name = name.equals(DEFAULT_NAME) ? DEFAULT_NAME+":"+id : name;
    }

    public GameObject(String name){
        this("", name);
    }

    public GameObject(){
        this("", DEFAULT_NAME);
    }

    public void putIntoMap(Place map){
        //Debug.toLog(this + " into place "+map);
        this.objectPlace = map;
        this.objectOrigin = map.getOriginFromPivot();
        map.objectsInPlace.add(this);
    }

    public void placeIntoPosition(Position position){
        this.objectPosition = position;
    }

    @Override
    public String toString(){
        return name;
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
        //Debug.toLog(objectPlace);
        return objectPlace;
    }

    public Position getObjectPosition() {
        return objectPosition;
    }

    public Direction getObjectOrigin(){
        return objectOrigin;
    }

    public void setName(String name){
        this.name = name;
    }
}
