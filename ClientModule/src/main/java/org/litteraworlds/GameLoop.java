package org.litteraworlds;

import org.litteraworlds.game.GameLogic;
import org.litteraworlds.game.MapGeneration;
import org.litteraworlds.input.Command;
import org.litteraworlds.map.Region;
import org.litteraworlds.objects.Creature;
import org.litteraworlds.objects.Player;
import org.litteraworlds.view.Debug;
import org.litteraworlds.view.colors.TextColors;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;

import static org.litteraworlds.input.PlayerInput.inputCommand;

public class GameLoop {

    private static Player player;

    private Region region;

    public static Player getPlayer(){
        Debug.toLog("Getting player "+player);
        return player;
    }

    public void playerCreation(){
        GameScreen.putString(TextColors.GAME_MESSAGE,"Посреди осколков старого мира появляется таинственный герой");
        GameScreen.putString(TextColors.GAME_MESSAGE,"Представься же:");

        String name = inputCommand();

        GameScreen.putString(TextColors.PLAYER_COLOR, "Твоё имя - "+name);

        GameScreen.putString(TextColors.GAME_MESSAGE,"Если твою личность ещё предстоит раскрыть в путешествии, то" +
                        "с характеристиками можно решить уже сейчас.");

        player = new Player(name);

        GameScreen.putString(player.getAbilities().toString());

        int available = Creature.Abilities.class.getDeclaredFields().length;
        int points;

        GameScreen.putString(TextColors.NARRATOR_COLOR,"У тебя есть "+available+" очков. Почему именно столько? Ну так вышло." +
                " Почему ты спрашиваешь про очки, но не о том, что с тобой разговаривает кто-то?");
        GameScreen.putString(TextColors.NARRATOR_COLOR,"Итак, начнём с атаки, сколько добавишь?");
        GameScreen.putString(TextColors.HELP_MESSAGE, "Просто введи цифру");

        available -= points = getPoints(inputCommand(), available);
        player.getAbilities().setAtk(player.getAbilities().getAtk() + points);

        GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось "+available+" очков");

        if(available > 0) {
            GameScreen.putString(TextColors.NARRATOR_COLOR,"Давай на защиту накинем.");
            available -= points = getPoints(inputCommand(), available);
            player.getAbilities().setDef(player.getAbilities().getDef() + points);

            GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось "+available+" очков");
        }

        if(available > 0) {
            GameScreen.putString(TextColors.NARRATOR_COLOR,"Насколько ты ловкий?");
            available -= points = getPoints(inputCommand(), available);
            player.getAbilities().setDex(player.getAbilities().getDex() + points);


            GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось " + available + " очков");
        }

        if(available > 0) {
            GameScreen.putString(TextColors.NARRATOR_COLOR,"Добавим к крепости духа?");
            available -= points = getPoints(inputCommand(), available);
            player.getAbilities().setDex(player.getAbilities().getDex() + points);
        }

        GameScreen.putString(TextColors.HELP_MESSAGE, "Очков не осталось");

        GameScreen.putString(TextColors.NARRATOR_COLOR,"Ну что же, ты готов к приключениям?");

        GameScreen.putString(TextColors.NARRATOR_COLOR, "Вводи что хочешь - ты всё равно отправишься в путь");

        inputCommand();
    }

    public int getPoints(String input, int available){
        try{
            while (true) {
                int points = Integer.parseInt(input);

                if (points <= available) {
                    return points;
                } else if (available > 0) {
                    GameScreen.putString(TextColors.GAME_MESSAGE, "Введено очков больше, чем доступно");

                    return getPoints(inputCommand(), available);
                } else {
                    return 0;
                }
            }
        } catch (NumberFormatException e) {
            GameScreen.putString(MessageType.ERROR, "Это не число!");
            GameScreen.putString(TextColors.GAME_MESSAGE, "Попробуй ещё раз:");
            return getPoints(inputCommand(), available);
        }
    }

    public void start() {
        GameScreen.putString(MessageType.SYSTEM, "Запуск игрового цикла");
        region = MapGeneration.generateNewRegion();
        region.putPlayerIntoRandomZone(player);

        GameScreen.putString(TextColors.PLAYER_COLOR, "Вы оказываетесь в регионе "+region+" в зоне "+player.getObjectPlace());
        GameLogic.lookAround();
        GameScreen.putString(TextColors.GAME_MESSAGE,"Что будете делать дальше?");
        GameScreen.putString(TextColors.HELP_MESSAGE,"Наберите /помощь для списка команд");

        String commandLine;
        while (!(commandLine = inputCommand()).equals("/выход")){
            if(commandLine.startsWith("/")){
                Command.parse(commandLine);
            }
        }
    }

}
