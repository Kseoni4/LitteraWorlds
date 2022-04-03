package org.litteraworlds.objects;

import org.litteraworlds.view.colors.Colors;

import java.util.ArrayList;
import java.util.Optional;

public class Player extends Creature {

    private String tokenID;

    private final ArrayList<Item> inventory = new ArrayList<>();

    public Player(String name) {
        super(Colors.GREEN_PASTEL,name);
        this.getAbilities().setAbilities(2,2,2,2);
    }

    public void putIntoInventory(Item item) {
        this.inventory.add(item);
    }

    public Item getFromInventory(Item item) {
        Optional<Item> optionalItem = this.inventory.stream().filter(itm -> itm.getID() == item.getID()).findFirst();
        return optionalItem.orElseThrow();
    }
}
