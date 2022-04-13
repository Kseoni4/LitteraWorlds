package org.litteraworlds.objects;

import org.litteraworlds.view.colors.Colors;

import java.io.Serializable;

public class Creature extends GameObject {

    private int hp;

    private int level;

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
}
