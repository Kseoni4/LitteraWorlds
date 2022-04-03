package org.litteraworlds.game;

import org.litteraworlds.GameLoop;
import org.litteraworlds.input.Command;
import org.litteraworlds.input.PlayerInput;
import org.litteraworlds.map.Direction;
import org.litteraworlds.map.Position;
import org.litteraworlds.objects.GameObject;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.LinesType;
import org.litteraworlds.view.TextLines;
import org.litteraworlds.view.colors.TextColors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * <h3>[CLIENT-SIDE]</h3>
 * <h4>GameLogic</h4>
 * Класс, содержаший статические методы для реализации логики соответствующих доступных команд (cм.класс {@link Command}).
 * <br>Методы могут иметь перегруженные аналоги для команд с дополнительными аргументами, например:
 * <br>{@code walkTo()} - "пойти в некоторую сторону" - имеет перегрузку {@code walkTo(String direction)}, которая принимает указание стороны света
 * (см.перечисление {@link Direction})
 */
public class GameLogic {
    private GameLogic(){}

    public static void walkTo(){
        GameScreen.putString(TextLines.getLine(LinesType.CHOOSE_DIRECTION1));
        walkTo(PlayerInput.inputCommand());
    }

    public static void walkTo(String direction) {
        Position nextPosition = new Position(Direction.findByLocale(direction));
        GameLoop.getPlayer().placeIntoPosition(nextPosition);
        GameScreen.putString(TextLines.getLine(LinesType.YOU_GOING_TO, GameLoop.getPlayer().getObjectPosition().toString().toLowerCase()));
    }

    public static void lookAround() {
        GameScreen.putString(TextColors.PLAYER_COLOR,"Вы осматриваетесь вокруг");
        ArrayList<GameObject> gameObjects = GameLoop.getPlayer().getObjectPlace().objectsInPlace;

        for (Direction direction : Direction.values()) {
            for (GameObject go : gameObjects) {
                if (go.getObjectPosition().getOrientation().equals(direction) && !go.equals(GameLoop.getPlayer())) {
                    GameScreen.putString("В стороне "+direction+" вы видете "+go);
                }
            }
        }
    }

    public static void whereAmI(){
        String currentLocation = GameLoop.getPlayer().getObjectPlace().toString();
        GameScreen.putString(TextColors.PLAYER_COLOR, "Вы находитесь в "+currentLocation);
    }

    public static void help(){
        GameScreen.putString(TextColors.GAME_MESSAGE, "=== Список доступных команд ===");
        Map<String, Method> commandList = Command.getCommandList();
        for(String s : commandList.keySet()){
            GameScreen.putString(TextColors.HELP_MESSAGE, "Команда /"+s);
        }
    }
}
