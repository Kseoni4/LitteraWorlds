package org.litteraworlds.dto;

import java.io.Serializable;
import java.util.List;

/**
 * <h2>[CLIENT\SERVER-SIDE]</h2>
 * <h3>Player Data Transfer Object</h3>
 * Контейнер для передачи данных игрока между клиентом и сервером<br>
 * Содержит все свойства персонажа<br>
 * SerialVersionUID = {@value #serialVersionUID} - версия контейнера
 */
public class PlayerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String playerName;

    private String tokenID;

    private List<Object> inventory;

    private List<Integer> abilitiesNumbers;

    private int hp;

    private int level;

    private String playerPlaceHashID;

    public int getHp() {
        return hp;
    }

    public int getLevel() {
        return level;
    }

    public String getPlayerPlaceHashID() {
        return playerPlaceHashID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTokenID() {
        return tokenID;
    }

    public List<Object> getInventory() {
        return inventory;
    }

    public List<Integer> getAbilities() {
        return abilitiesNumbers;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "playerName='" + playerName + '\'' +
                ", tokenID='" + tokenID + '\'' +
                ", inventory=" + inventory +
                ", abilitiesNumbers=" + abilitiesNumbers +
                ", hp=" + hp +
                ", level=" + level +
                ", playerPlaceHashID='" + playerPlaceHashID + '\'' +
                '}';
    }
}
