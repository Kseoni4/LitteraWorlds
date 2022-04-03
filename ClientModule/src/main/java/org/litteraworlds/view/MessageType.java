package org.litteraworlds.view;

import org.litteraworlds.view.colors.Colors;

public enum MessageType {
    SYSTEM(Colors.GOLDEN+"[СИСТЕМА]"),
    PLAYER(Colors.GREEN_PASTEL+"[ИГРОК]"),
    FIGHT(Colors.ORANGE+"[БОЙ]"),
    INFO(Colors.GREY+"[ИНФО]"),
    ERROR(Colors.RED_BRIGHT+"[ОШИБКА]");

    private final String locale;

    MessageType(String locale){
        this.locale = locale;
    }

    @Override
    public String toString(){
       return locale;
    }
}
