package org.litteraworlds;

import org.litteraworlds.game.GameLogic;
import org.litteraworlds.game.PlayerCreation;
import org.litteraworlds.game.WorldGenerator;
import org.litteraworlds.input.Command;
import org.litteraworlds.map.Region;
import org.litteraworlds.net.Requests;
import org.litteraworlds.objects.Player;
import org.litteraworlds.utils.HashToString;
import org.litteraworlds.view.Debug;
import org.litteraworlds.view.colors.TextColors;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;
import org.litteraworlds.workers.ConnectionWorker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.litteraworlds.input.PlayerInput.inputCommand;

/**
 * <h2>[CLIENT-SIDE]</h2>
 * <h3>GameLoop</h3>
 * Класс, описывающий поведение игрового цикла<br>
 * Содержит функции для создания игрока и его регистрации на сервере.
 */
public class GameLoop {

    private static Player player;

    private ConnectionWorker connectionWorker;

    private Region region;

    public GameLoop(ConnectionWorker connectionWorker){
        this.connectionWorker = connectionWorker;

        GameScreen.putString(TextColors.GAME_MESSAGE,"Посреди осколков старого мира появляется таинственный герой");

        //Вызов функции создания персонажа
        player = PlayerCreation.newCreationSequence().createNewCharacter();
    }

    public static Player getPlayer(){
        Debug.toLog("Getting player "+player);
        return player;
    }

    public void start() throws IOException, NoSuchAlgorithmException {
        GameScreen.putString(MessageType.SYSTEM, "Запуск игрового цикла");

        //Отравляем данные об игроке на сервер для получения хэша генерации мира
        connectionWorker.sendToServer(player.toString().getBytes(StandardCharsets.UTF_8));

        GameScreen.putString(MessageType.SYSTEM,"Ожидание хэша для генерации");
        Requests.setConnectionWorker(connectionWorker);

        byte[] hash = Requests.getHash();
        String hashLine = HashToString.convert(hash);

        Debug.toLog("h length "+hashLine.length());
        Debug.toLog("hash:"+hashLine);

        GameScreen.putString(MessageType.INFO, hashLine);

        region = WorldGenerator.getWorld().getRegions().get(0);

        Debug.toLog("Помещаем игрока с случайную зону");
        region.putPlayerIntoRandomZone(player);

        /* Отправляем данные персонажа на сервер */
        GameScreen.putString(MessageType.SYSTEM,"Регистрация игрока на сервере");
        GameScreen.putString(MessageType.SYSTEM,"Отправка данных...");
        if(sendPlayerDataToServer()){
            GameScreen.putString(MessageType.SYSTEM,"Регистрация прошла успешно!");

            //TODO вот здесь надо вывести на экран сгенерированный на стороне клиента пароль
            /*
                SecretKey key = Request.getSecretKeyFromServer();
                player.setTokenID(key.getEncoded);
                String password = PasswordGen.getRandomPasswordByKey(key);

                GameScreen.putString(MessageType.SYSTEM,"Ваш пароль для входа в игру");
                GameScreen.putString(MessageType.SYSTEM, password);

                //Далее хэшируем пароль и отправляем на сервер
                byte[] passwordHash = HashGen.getHash(password)
                Request.sendPasswordHashToServer(passwordHash);
             */

            gameStart();
        } else {
            GameScreen.putString(MessageType.ERROR,"Проблемы с регистрацией");
            GameScreen.putString(MessageType.INFO,"Нажмите любую кнопку для выхода");
            inputCommand();
        }
    }

    private boolean sendPlayerDataToServer(){
        return Requests.registerPlayer(player);
    }

    /**
     * Запуск игрового процесса
     */
    private void gameStart(){
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
