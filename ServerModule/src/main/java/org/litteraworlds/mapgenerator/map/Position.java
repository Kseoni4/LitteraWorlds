package org.litteraworlds.mapgenerator.map;

import com.googlecode.lanterna.TerminalPosition;

public class Position {

    private Direction direction;

    public int x;

    public int y;

    public Position(Direction direction){
        this.direction = direction;
    }

    public Position(int x, int y){
        this.x = x;
        this.y = y;
        this.direction = Direction.NORTH;
    }

    public Position(TerminalPosition terminalPosition){
        this.x = terminalPosition.getColumn();
        this.y = terminalPosition.getRow();
    }

    public TerminalPosition getTerminalPosition(){
        return new TerminalPosition(x,y);
    }

    public String getPositionLocale(){
        return this.direction.toString();
    }

    public Direction getOrientation(){
        return this.direction;
    }


    @Override
    public String toString(){
        return direction.toString();
    }
}
