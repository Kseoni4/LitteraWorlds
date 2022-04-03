package org.litteraworlds.map;

import org.litteraworlds.view.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Direction {
    NORTH("Север"),
    SOUTH("Юг"),
    EAST("Восток"),
    WEST("Запад"),
    NORTH_EAST("Северо-восток"),
    NORTH_WEST("Северо-запад"),
    SOUTH_EAST("Юго-восток"),
    SOUTH_WEST("Юго-запад");

    private final String ruLocale;

    public static Direction findByLocale(String locale){
        try {
           return Arrays.stream(Direction.values()).filter(direction -> direction.ruLocale.equalsIgnoreCase(locale)).findFirst().get();
        } catch (NoSuchElementException e){
            GameScreen.putString(MessageType.ERROR, TextLines.getLine(LinesType.UNKNOWN_DIRECTION));
        }
        return null;
    }

    Direction(String locale){
        this.ruLocale = locale;
    }

    @Override
    public String toString() {
        return ruLocale;
    }
}
