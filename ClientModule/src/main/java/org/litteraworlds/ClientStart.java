package org.litteraworlds;

import org.litteraworlds.input.Command;
import org.litteraworlds.net.SecurityConfig;
import org.litteraworlds.view.*;
import org.litteraworlds.view.colors.TextColors;
import org.litteraworlds.workers.ConnectionWorker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.litteraworlds.input.PlayerInput.inputCommand;


/**
 * <h3>[CLIENT-SIDE]</h3>
 * <h4>ClientStart</h4>
 * Класс - точка входа в программу
 * Инициализирует основные игровые ресурсы:
 *
 * <li>
 *    Окно с текстовым интерефейсом (см.класс {@link GameScreen});
 * </li>
 * <li>
 *     Текстовые данные для вывода на экран: стандартные фразы и текстовые абзацы (см.класс {@link TextLines});
 * </li>
 * <li>
 *     Список комманд, доступных для ввода игроком (см.класс {@link Command});
 * </li>
 * После инициализации запускает процедуру создания игрока и далее игровой цикл {@link GameLoop} для ввода команд.
 */
final class ClientStart {

    static ExecutorService workers = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        //new SecurityConfig().testConfig();
        GameScreen.init();
        GameScreen.putString(MessageType.SYSTEM, "Запуск игрового клиента");
        GameScreen.putString(MessageType.SYSTEM, "Оконная форма инциализирована");
        TextLines.init();
        GameScreen.putString(MessageType.SYSTEM, "Текстовые данные загружены");
        Command.init();
        GameScreen.putString(MessageType.SYSTEM, "Игровые команды инициализированы");

        workers.execute(new ConnectionWorker());

        GameScreen.putString(TextColors.HELP_MESSAGE,"Для начала игры, введите команду /старт");
        while (true) {
            String command = inputCommand();
            switch (command) {
                case "/старт" -> {

                    for (String line : TextLines.getIntrolines()) {
                        GameScreen.putString(line);
                    }

                    inputCommand();

                    GameLoop gameLoop = new GameLoop();

                    gameLoop.playerCreation();

                    gameLoop.start();

                    GameScreen.dispose();
                }
                case "/выход" -> {
                    GameScreen.dispose();
                    return;
                }
            }
            if (GameScreen.isClose()) {
                break;
            }
        }
    }

    private ClientStart() {
    }
}
