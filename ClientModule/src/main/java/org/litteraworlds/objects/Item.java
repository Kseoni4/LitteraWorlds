package org.litteraworlds.objects;

import java.util.concurrent.ThreadLocalRandom;

public class Item extends GameObject {

    protected Rarity itemRarity;

    protected int price;

    public static final Item DEFAULT_ITEM = new Item(Rarity.COMMON, "DEFAULT ITEM");

    public Item(Rarity itemRarity, String name) {
        super(itemRarity.toString(), name);
        this.itemRarity = itemRarity;
        this.price = ThreadLocalRandom.current().nextInt(3,12 * itemRarity.getEmpower());
    }

    public int getPrice() {
        return price;
    }

    public Rarity getItemRarity() {
        return itemRarity;
    }
}
