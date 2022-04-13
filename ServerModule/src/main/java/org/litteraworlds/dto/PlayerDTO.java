package org.litteraworlds.dto;

import java.io.Serializable;
import java.util.List;

public class PlayerDTO implements Serializable {

    private static final long SerialVersionUID = 1L;

    private String playerName;

    private String tokenID;

    private List<Object> inventory;

    private List<Integer> abilitiesNumbers;

    private int hp;

    private int level;

    private String playerPlaceHashID;

    /*public PlayerDTO(Player player){
        this.playerName = player.getName();
        this.tokenID = player.getTokenID();
        this.inventory = player.getInventory();
        this.hp = player.getHp();
        this.level = player.getLevel();
        this.playerPlaceHashID = player.getObjectPlace().getPlaceHashID();
        abilitiesNumbers.add(player.getPlayerAbilities().getAtk());
        abilitiesNumbers.add(player.getPlayerAbilities().getDef());
        abilitiesNumbers.add(player.getPlayerAbilities().getDex());
        abilitiesNumbers.add(player.getPlayerAbilities().getStg());
    }*/

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
