package org.litteraworlds.objects;

import java.util.concurrent.ThreadLocalRandom;

public class Item extends GameObject {

    protected Rarity itemRarity;

    protected int price;

    protected String description;

    public Item(Rarity itemRarity, String name) {
        super(itemRarity.toString(), name);
        this.itemRarity = itemRarity;
        this.price = ThreadLocalRandom.current().nextInt(3,12 * itemRarity.getEmpower());
    }

    public Item(Rarity itemRarity, String name, String description){
        this(itemRarity, name);
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public Rarity getItemRarity() {
        return itemRarity;
    }

    public String getDescription() {
        return description;
    }
}
