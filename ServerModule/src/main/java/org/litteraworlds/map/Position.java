package org.litteraworlds.map;

public class Position {

    public int x;

    public int y;

/*    public Position(Direction direction){
        this.direction = direction;
    }*/

    public Position setPosition(Position position){
       return this.setPosition(position.x, position.y);
    }

    public Position setPosition(int x, int y){
        this.x = x;
        this.y = y;
        return this;
    }

    public Position getRelativePosition(Position position){
        return new Position(position.x + x, position.y + y);
    }

    public Position(Position position){
        this.x = position.x;
        this.y = position.y;
    }

    public Position(int x, int y){
        this.x = x;
        this.y = y;
        //this.direction = Direction.NORTH;
    }

/*    public String getPositionLocale(){
        return this.direction.toString();
    }

    public Direction getOrientation(){
        return this.direction;
    }*/


    @Override
    public String toString(){
        return "{%d;%d}".formatted(x,y);
    }
}
