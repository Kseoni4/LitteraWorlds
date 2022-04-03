package org.litteraworlds.map;

import org.litteraworlds.view.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * <h2>[CLIENT-SIDE]</h2>
 * <h3>Direction</h3>
 * <p>
 *     Перечисление доступных направлений, согласно "розе ветров":
 *
     * <ls>
     *     <li>Север (-восток, -запад)</li>
     *     <li>Юг (-восток, -запад)</li>
     *     <li>Восток</li>
     *     <li>Запад</li>
     * </ls>
 * </p>
 */
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
            Optional<Direction> optionalDirection = Arrays.stream(Direction.values()).filter(direction -> direction.ruLocale.equalsIgnoreCase(locale)).findFirst();
            return optionalDirection.orElseThrow();
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
