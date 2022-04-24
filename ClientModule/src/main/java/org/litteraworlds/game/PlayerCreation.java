package org.litteraworlds.game;

import org.litteraworlds.objects.Player;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;
import org.litteraworlds.view.colors.TextColors;

import static org.litteraworlds.input.PlayerInput.inputCommand;

public class PlayerCreation {

    Player player;

    private PlayerCreation(){}

    public static PlayerCreation newCreationSequence(){
        return new PlayerCreation();
    }

    /**
     * <h4>Функция создания персонажа</h4>
     * <p>
     *     Последовательно создаёт персонажа.
     *     Проводит проверку на доступность имени персонажа (то есть, имя уже не занято на стороне сервера)
     * </p>
     * @return нового игрока
     */
    public Player createNewCharacter(){
        GameScreen.putString(TextColors.GAME_MESSAGE,"Представься же:");

        String name = inputCommand();

        //TODO проверка имени пользователя на присутствие в базе данных
        /*
            !!!ПРИМЕР!!!
            while(!Request.nameIsAvailable(name) {
                GameScreen.putString(TextColors.GAME_MESSAGE,"Такое имя уже занято! Введите другое:");
                name = inputCommand();
            }
         */

        GameScreen.putString(TextColors.PLAYER_COLOR, "Твоё имя - "+name);
        GameScreen.putString(TextColors.NARRATOR_COLOR,
                "Если твою личность ещё предстоит раскрыть в путешествии, то" +
                "с характеристиками можно решить уже сейчас.");
        GameScreen.putEmptyString();

        player = new Player(name);

        GameScreen.putString(TextColors.NARRATOR_COLOR,"Сейчас твои характеристики выглядят максимально средне:");
        GameScreen.putString(player.getPlayerAbilities().toString());
        GameScreen.putEmptyString();

        int available = Player.Abilities.class.getDeclaredFields().length + 1;
        int points;

        GameScreen.putString(TextColors.NARRATOR_COLOR,"У тебя есть "+available+" очков. Почему именно столько? Ну так вышло." +
                " Почему ты спрашиваешь про очки, но не о том, что с тобой разговаривает кто-то?");

        int abilityIndex = 0;

        while(available > 0){
            showReplica(abilityIndex);

            if(abilityIndex > 3)
                abilityIndex = 0;

            GameScreen.putString(TextColors.HELP_MESSAGE, "Осталось "+available+" очков");

            available -= points = getPoints(inputCommand(), available);

            changeAbilityForPointSet(abilityIndex, points);

            abilityIndex++;
            GameScreen.putEmptyString();
        }

        GameScreen.putString(TextColors.HELP_MESSAGE, "Очков не осталось");
        GameScreen.putEmptyString();

        GameScreen.putString(TextColors.HELP_MESSAGE, "Посмотрим что получилось:");
        GameScreen.putString(player.getPlayerAbilities().toString());
        GameScreen.putEmptyString();

        GameScreen.putString(TextColors.NARRATOR_COLOR,"Ну что же, ты готов к приключениям?");
        GameScreen.putString(TextColors.NARRATOR_COLOR,"Я уверен, что да.");
        GameScreen.putString(TextColors.NARRATOR_COLOR, "Вводи что хочешь - ты всё равно отправишься в путь");

        inputCommand();

        return player;
    }

    private int getPoints(String input, int available){
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

    private void showReplica(int i){
        switch (i){
            case 0 -> {
                GameScreen.putString(TextColors.NARRATOR_COLOR,"Итак, начнём с атаки, сколько добавишь?");
                GameScreen.putString(TextColors.HELP_MESSAGE, "Просто введи цифру");
            }
            case 1 -> GameScreen.putString(TextColors.NARRATOR_COLOR,"Давай на защиту накинем.");
            case 2 -> GameScreen.putString(TextColors.NARRATOR_COLOR,"Насколько ты ловкий?");
            case 3 -> GameScreen.putString(TextColors.NARRATOR_COLOR,"Добавим к крепости духа?");
            case 4 -> GameScreen.putString(TextColors.NARRATOR_COLOR,"Похоже у тебя остались ещё очки, может ещё добавишь куда?");
        }
    }

    private void changeAbilityForPointSet(int i, int points){
        switch (i){
            case 0 -> player.getPlayerAbilities().setAtk(player.getPlayerAbilities().getAtk() + points);
            case 1 -> player.getPlayerAbilities().setDef(player.getPlayerAbilities().getDef() + points);
            case 2 -> player.getPlayerAbilities().setDex(player.getPlayerAbilities().getDex() + points);
            case 3 -> player.getPlayerAbilities().setStg(player.getPlayerAbilities().getStg() + points);
        }
    }
}
