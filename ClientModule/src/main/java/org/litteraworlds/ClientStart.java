package org.litteraworlds;

import org.litteraworlds.input.Command;
import org.litteraworlds.view.*;
import org.litteraworlds.view.colors.TextColors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.litteraworlds.input.PlayerInput.inputCommand;

final class ClientStart {

    public static void main(String[] args) throws IOException {
        GameScreen.init();
        GameScreen.putString(MessageType.SYSTEM, "Запуск игрового клиента");
        GameScreen.putString(MessageType.SYSTEM, "Оконная форма инциализирована");
        TextLines.init();
        GameScreen.putString(MessageType.SYSTEM, "Текстовые данные загружены");
        Command.init();
        GameScreen.putString(MessageType.SYSTEM, "Игровые команды инициализированы");

        /*Creature creature = new Creature("Тестовое существо");

        GameScreen.putGameObjectName(creature);

        GameScreen.putString("01234567890");

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item(Rarity.COMMON, "обычная шмотка"));
        items.add(new Item(Rarity.GOOD, "норм шмотка"));
        items.add(new Item(Rarity.RARE, "редкая шмотка"));
        items.add(new Item(Rarity.EPIC, "эпичная шмотка"));
        GameObject unnamedGO = new GameObject();

        for(GameObject go : items){
            GameScreen.putGameObjectName(go);
            GameScreen.putString(TextLines.getLine(LinesType.PICK_UP_ITEM, go));
        }
        GameScreen.putGameObjectName(unnamedGO);

        GameScreen.putString(TextLines.getLine(LinesType.LOOK_UP_INVENTORY, items));

        GameScreen.showBufferLine(0);*/

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
