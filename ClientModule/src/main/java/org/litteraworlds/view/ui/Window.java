package org.litteraworlds.view.ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.colors.Colors;

public abstract class Window implements Initable, Drawable, Resetable {

    private TerminalPosition positionOnScreen;

    private TerminalPosition innerZeroPoint;

    private TerminalSize windowSize;

    private TextGraphics windowGraphics;

    private String windowName;

    private int windowId;

    public final TerminalPosition getPositionOnScreen() {
        return positionOnScreen;
    }

    public final void setPositionOnScreen(TerminalPosition positionOnScreen) {
        this.positionOnScreen = positionOnScreen;
    }

    public final TerminalSize getWindowSize() {
        return windowSize;
    }

    public final void setWindowSize(TerminalSize windowSize) {
        this.windowSize = windowSize;
    }

    public final void setWindowSize(int w, int h){
        this.windowSize.with(new TerminalSize(w,h));
    }

    public final String getWindowName() {
        return windowName;
    }

    public final int getWindowId() {
        return windowId;
    }

    private static boolean active;

    public static boolean isActive(){
        return active;
    }

    public Window build(){
        if(GameScreen.isActive()){

            TextCharacter wall = new TextCharacter('*').withForegroundColor(Colors.getTextColor(Colors.ORANGE));

            BasicTextImage basicTextImage = new BasicTextImage(this.windowSize);

            for(int x = 0; x < basicTextImage.getSize().getColumns(); x++){
                basicTextImage.setCharacterAt(x,0, wall);
            }

            for(int y = 0; y < basicTextImage.getSize().getRows(); y++){
                basicTextImage.setCharacterAt(0, y, wall);
            }

            for(int x = 1; x < basicTextImage.getSize().getColumns(); x++){
                basicTextImage.setCharacterAt(x, basicTextImage.getSize().getRows()-1, wall);
            }

            for(int y = 1; y < basicTextImage.getSize().getRows(); y++){
                basicTextImage.setCharacterAt(basicTextImage.getSize().getColumns()-1, y, wall);
            }

            basicTextImage.newTextGraphics().putString(basicTextImage.getSize().getColumns()/2, 0, windowName);

            windowGraphics.drawImage(new TerminalPosition(0,0), basicTextImage);

        }
        return this;
    }

    public void placeStringIntoWindow(String color, String s, int x){
        GameScreen.putString(color, s, x);
    }

    public Window(String name, TerminalSize windowSize, TerminalPosition positionOnScreen){
        this.windowName = name;
        this.windowSize = windowSize;
        this.positionOnScreen = positionOnScreen;
        this.innerZeroPoint = positionOnScreen;
        this.windowGraphics = GameScreen.getNewGraphics();
    }

    public Window(String name, TerminalPosition positionOnScreen){
        this(name, GameScreen.getScreenSize(), positionOnScreen);
    }
}
