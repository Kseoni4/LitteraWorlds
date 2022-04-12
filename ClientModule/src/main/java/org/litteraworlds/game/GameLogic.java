package org.litteraworlds.game;

import org.litteraworlds.GameLoop;
import org.litteraworlds.input.Command;
import org.litteraworlds.input.PlayerInput;
import org.litteraworlds.map.Building;
import org.litteraworlds.map.Direction;
import org.litteraworlds.map.Place;
import org.litteraworlds.map.Position;
import org.litteraworlds.objects.GameObject;
import org.litteraworlds.view.Debug;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.LinesType;
import org.litteraworlds.view.TextLines;
import org.litteraworlds.view.colors.TextColors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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

    public static void walkToObject(String objectName){
        Place place = GameLoop.getPlayer().getObjectPlace();
        GameObject go = getGameObjectIfExists(objectName);
        Debug.toLog(place);
        Debug.toLog(objectName);
        if(go != null){
            Debug.toLog(go.getName());
            List<GameObject> gameObjects = place.findObjectsInPlace(go);
            if(gameObjects.size() > 1){
                GameScreen.putString("К какому из?");
                for(GameObject gameObject : gameObjects){
                    GameScreen.putString(gameObjects.indexOf(gameObject)+gameObject.toString());
                }
            } else {
                GameScreen.putString("Вы идёте к "+gameObjects.get(0));
                GameLoop.getPlayer().placeIntoPosition(gameObjects.get(0).getObjectPosition());
            }
            GameScreen.putString("Вы стоите перед "+ go);
        }
    }

    public static void enterBuilding(String buildingName) {
        Building building = (Building) GameLoop.getPlayer().getObjectPlace().findObjectInPlace(buildingName);
        Debug.toLog(building);
        Debug.toLog(building.getObjectPlace());
        Debug.toLog(building.getObjectPosition());
        if(building != null) {
            GameScreen.putString(TextColors.PLAYER_COLOR, "Вы входите в здание");
            GameLoop.getPlayer().putIntoMap(building.getFirstFloor().getRoomsOnFloor().get(0));
            Debug.toLog(GameLoop.getPlayer().getObjectPlace());
            GameScreen.putString(TextColors.PLAYER_COLOR, "Вы находитесь на первом этаже в первой комнате");
            GameScreen.putString(TextColors.PLAYER_COLOR, GameLoop.getPlayer().getObjectPlace().toString());
        } else {
            GameScreen.putString(TextColors.ERROR_MESSAGE, "Некуда входить");
        }
    }

    public static void lookAround() {
        GameScreen.putString(TextColors.PLAYER_COLOR,"Вы осматриваетесь вокруг");
        ArrayList<GameObject> gameObjects = GameLoop.getPlayer().getObjectPlace().objectsInPlace;

        for (Direction direction : Direction.values()) {
            Debug.toLog("LOOK_AROUND|Direction:"+direction);
            for (GameObject go : gameObjects) {
                if (go.getObjectPosition().getOrientation().equals(direction) && !go.equals(GameLoop.getPlayer())) {
                    Debug.toLog("LOOK_AROUND|Object in direction:"+direction+" -> "+go);
                    GameScreen.putString("В стороне "+direction+" вы видете "+go);
                }
            }
        }
    }

    private static GameObject getGameObjectIfExists(String objectName){
        Place place = GameLoop.getPlayer().getObjectPlace();
        Debug.toLog("Search for object "+objectName);
        return place.findObjectInPlace(objectName);
    }

    private static List<GameObject> getAllObjectsInPlace(GameObject object){
        Place place = GameLoop.getPlayer().getObjectPlace();
        return place.findObjectsInPlace(object);
    }

    private static void checkPlayerPosition(){

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
