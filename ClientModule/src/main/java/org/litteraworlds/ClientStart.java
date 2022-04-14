package org.litteraworlds;

import org.litteraworlds.input.Command;
import org.litteraworlds.view.*;


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

    private static GameInstance gameInstance = new GameInstance();
    public static void main(String[] args) {
        gameInstance.startSequence();
    }
}
