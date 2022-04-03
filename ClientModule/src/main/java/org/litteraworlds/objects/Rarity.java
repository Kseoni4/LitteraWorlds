package org.litteraworlds.objects;

import org.litteraworlds.view.colors.Colors;

public enum Rarity {
    COMMON(Colors.GREY_243, 1),
    GOOD(Colors.GREEN_PASTEL, 2),
    RARE(Colors.MAGENTA, 3),
    EPIC(Colors.ORANGE, 6);

    private final String color;

    private final int empower;

    Rarity(String color, int empower) {
        this.color = color;
        this.empower = empower;
    }

    public int getEmpower() {
        return empower;
    }

    @Override
    public String toString() {
        return color;
    }
}
