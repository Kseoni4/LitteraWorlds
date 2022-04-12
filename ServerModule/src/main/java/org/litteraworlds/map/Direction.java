package org.litteraworlds.map;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * <h2>[SERVER-SIDE]</h2>
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

/*    public static Direction findByLocale(String locale){
        try {
            Optional<Direction> optionalDirection = Arrays.stream(Direction.values()).filter(direction -> direction.ruLocale.equalsIgnoreCase(locale)).findFirst();
            return optionalDirection.orElseThrow();
        } catch (NoSuchElementException e){
            Logger.getGlobal().info("НЕИЗВЕСТНОЕ НАПРАВЛЕНИЕ");
        }
        return null;
    }*/

    public static Direction calculateNextPlaceDirection(Position currentPlace, Position nextPlace){
        boolean isWest = isWest(currentPlace, nextPlace);
        boolean isNorth = isNorth(currentPlace, nextPlace);
        boolean isSouth = isSouth(currentPlace, nextPlace);
        boolean isEast = isEast(currentPlace, nextPlace);

        if(isNorth) {
            if (!isWest && !isEast) {
                return NORTH;
            } else if (isWest) {
                return NORTH_WEST;
            } else {
                return NORTH_EAST;
            }
        } else if(isSouth) {
            if (!isWest && !isEast) {
                return SOUTH;
            } else if (isWest) {
                return SOUTH_WEST;
            } else {
                return SOUTH_EAST;
            }
        } else {
            return null;
        }
    }


    private static boolean isNorth(Position p1, Position p2){
        return p1.y >= p2.y;
    }

    private static boolean isSouth(Position p1, Position p2){
        return p1.y <= p2.y;
    }

    private static boolean isEast(Position p1, Position p2){
        return p1.x <= p2.x;
    }

    private static boolean isWest(Position p1, Position p2){
        return p1.x >= p2.x;
    }

    Direction(String locale){
        this.ruLocale = locale;
    }

    @Override
    public String toString() {
        return ruLocale;
    }
}
