package org.litteraworlds.objects;

import org.litteraworlds.view.colors.Colors;

public class Creature extends GameObject {

    private int hp;

    private int level;

    private final Abilities abilities;

    public Creature(String name) {
        this(Colors.GREY, name);
    }

    public Creature(String color, String name){
        this(color, name, 1, 1);
    }

    public Creature(String color, String name, int hp, int level){
        super(color, name);
        this.hp = hp;
        this.level = level;
        abilities = new Abilities(0,0,0,0);
    }

    public Abilities getAbilities(){
        return this.abilities;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static class Abilities {
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
