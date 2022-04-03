package org.litteraworlds.objects;

import java.util.concurrent.ThreadLocalRandom;

public class Weapon extends Item {

    private final int damage;

    private WeaponType weaponType;

    public Weapon(Rarity itemRarity, String name) {
        super(itemRarity, name);
        this.damage = ThreadLocalRandom.current().nextInt(5, itemRarity.getEmpower() * 11);
    }

    public int getDamage() {
        return damage;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }
}

