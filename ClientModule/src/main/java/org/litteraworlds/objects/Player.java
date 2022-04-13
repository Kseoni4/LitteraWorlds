package org.litteraworlds.objects;

import org.litteraworlds.view.colors.Colors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player extends Creature {

    private String tokenID;

    private final ArrayList<Item> inventory = new ArrayList<>();

    private Abilities playerAbilities;

    public Player(String name) {
        super(Colors.GREEN_PASTEL,name);
        this.playerAbilities = new Abilities(2,2,2,2);
    }

    public void putIntoInventory(Item item) {
        this.inventory.add(item);
    }

    public Item getFromInventory(Item item) {
        Optional<Item> optionalItem = this.inventory.stream().filter(itm -> itm.getID() == item.getID()).findFirst();
        return optionalItem.orElseThrow();
    }

    public List<Item> getInventory(){
        return inventory;
    }
    public String getTokenID() {
        return tokenID;
    }

    public Abilities getPlayerAbilities(){
        return this.playerAbilities;
    }

    public static class Abilities implements Serializable {
        private int atk;

        private int def;

        private int dex;

        private int stg;

        public int getAtk() {
            return atk;
        }

        public int getDef() {
            return def;
        }

        public int getDex() {
            return dex;
        }

        public int getStg() {
            return stg;
        }

        public void setAtk(int atk) {
            this.atk = atk;
        }

        public void setDef(int def) {
            this.def = def;
        }

        public void setDex(int dex) {
            this.dex = dex;
        }

        public void setStg(int stg) {
            this.stg = stg;
        }

        public Abilities(int atk, int def, int dex, int stg) {
            this.atk = atk;
            this.def = def;
            this.dex = dex;
            this.stg = stg;
        }

        public void setAbilities(int atk, int def, int dex, int stg){
            this.atk = atk;
            this.def = def;
            this.dex = dex;
            this.stg = stg;
        }

        @Override
        public String toString() {
            return "Характеристики:"+
                    " Сила атаки: "+atk+
                    " Защита: "+def+
                    " Ловкость: "+dex+
                    " Крепость духа: "+stg;
        }
    }
}
