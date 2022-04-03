package org.litteraworlds.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import org.litteraworlds.objects.GameObject;
import org.litteraworlds.view.colors.Colors;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GameScreen {
    private GameScreen(){}

    private static final int ERROR_CODE_INIT_FAILED = 1;

    private static final int SUCCESS_CODE = 0;

    private static final int START_X = 1;

    private static TerminalScreen screen;

    private static TextGraphics screenText;

    private static TextGraphics screenPrompt;

    private static int yPointer = 0;

    private static int xPointer = START_X;

    private static int linePointer = 0;

    private static ArrayList<LineBuffer> lineBuffers = new ArrayList<>();

    public static int init() {
        try {
            DefaultTerminalFactory dtf = new DefaultTerminalFactory();

            File fontFile = new File(GameScreen.class.getResource("/FontFileIBM.ttf").toURI());

            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(18f);

            dtf.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.newInstance(font));

            screen = dtf.createScreen();

            screen.setCursorPosition(null);

            screen.getTerminal().setCursorVisible(false);

            screen.getTerminal().enterPrivateMode();

            screenText = screen.newTextGraphics();

            screenPrompt = screen.newTextGraphics();

            screenPrompt = screenPrompt.newTextGraphics(new TerminalPosition(0, screen.getTerminalSize().getRows()-1), new TerminalSize(16,1));

            screenPrompt.setCharacter(0,0,'>');

            lineBuffers.add(new LineBuffer());

            screen.startScreen();

        } catch (IOException e) {
            e.printStackTrace();
            return ERROR_CODE_INIT_FAILED;
        } catch (FontFormatException | URISyntaxException e) {
            e.printStackTrace();
        }

        return SUCCESS_CODE;
    }

    public static void resetAndClearPrompt(){
        xPointer = START_X;
        screenPrompt.fill(' ');
        screenPrompt.setCharacter(0, 0, '>');
        refresh();
    }

    public static void putGameObjectName(GameObject gameObject){
        putIntoScreenAndRefresh(gameObject.getColor(), gameObject.toString());
    }

    public static void putString(MessageType messageType, String msg){

        String message = msg + Colors.R;

        putIntoScreenAndRefresh(messageType.toString(), message);
    }

    public static void putString(String s){
        putString(Colors.GREY, s);
    }

    public static void putString(String color, String s){
        putIntoScreenAndRefresh(color, s+Colors.R);
    }

    private static void putIntoScreenAndRefresh(String color, String s){
        if(isTextLengthFit(s)) {
            //System.out.println(getTrimString(s));
            Debug.toLog("BEFORE INSERT LINE|yPointer = "+yPointer+"|LINE =" + s);
            screenText.putCSIStyledString(0, yPointer++, color+s);
            Debug.toLog("AFTER INSERT LINE|yPointer = "+yPointer);
        } else {
            putIntoScreenAndRefresh(color,getSplitString(color, s).toString());
            return;
        }
        lineBuffers.get(linePointer).putLineIntoBuffer(color+s);
        refresh();
    }

    private static StringBuilder getSplitString(String color, String s) {
        StringBuilder sb = new StringBuilder();

        String[] splitString = s.split(" ");

        int i = 0;

        do {
            sb.append(splitString[i]).append(" ");
            i++;
        } while (isTextLengthFit(sb.toString().concat(splitString[i])));

        putIntoScreenAndRefresh(color,sb.toString());

        sb = new StringBuilder();

        for(;i < splitString.length; i++){
            sb.append(splitString[i]).append(" ");
        }
        return sb;
    }

    public static KeyStroke getScreenInput() throws IOException {
        return screen.pollInput();
    }

    private static boolean isTextLengthFit(String text){
       return getTrimString(text).length() < screen.getTerminalSize().getColumns();
    }

    private static String getTrimString(String message){
        String trimmedMessage = message.trim();
        int len = trimmedMessage.split(" ").length;
        for(int i = 0; i <= len; i++) {
            trimmedMessage = trimmedMessage.replaceFirst("\\[\\d{2};\\d;((\\d{3}m)|(\\d{2}m)|(\\dm))", "");
            trimmedMessage = trimmedMessage.replaceFirst("\\d{2};\\d;((\\d{3}m)|(\\d{2}m)|(\\dm))", "");
            trimmedMessage = trimmedMessage.replaceFirst("\\dm", "");
            trimmedMessage = trimmedMessage.replaceFirst("(\\u001b|\\u001b[ESC]\\[(\\dm))", "");
            trimmedMessage = trimmedMessage.replaceFirst("(\\[(\\dm))", "");
            trimmedMessage = trimmedMessage.replaceFirst("\\[\\u001b|\\[ESC]", "");
        }
        return trimmedMessage;
    }

    public static void putChar(char s){
        screenText.setCharacter(xPointer++, screen.getTerminalSize().getRows()-1, new TextCharacter(s));
        //System.out.print(s);
        refresh();
    }

    public static void putCharIntoPrompt(char s, int linePointer){
        screenPrompt.setCharacter(linePointer, 0, s);
        //System.out.print(s);
        refresh();
    }

    public static void showBufferLine(int lineIndex) {
        LineBuffer lineBuffer = lineBuffers.get(lineIndex);
        for (String line : lineBuffer.lines) {
            System.out.println(line);
        }
    }

    public static void debugScroll(){
        screen.scrollLines(0, 5, 3);
        refresh();
    }

    private static void refresh(){
        try {
            screenText.clearModifiers();
            if(yPointer > screen.getTerminalSize().getRows()-1) {
                Debug.toLog("Screen rows: "+screen.getTerminalSize().getRows());
                Debug.toLog("yPointer: "+yPointer);

                int scrollDistance = yPointer/2;

                Debug.toLog("scrollDistance: "+scrollDistance);

                int topLine = 0;

                Debug.toLog("scroll parameters: "+topLine+"; "+yPointer+"; "+scrollDistance);

                screen.scrollLines(topLine,  yPointer, scrollDistance);

                yPointer = scrollDistance;

                linePointer++;

                lineBuffers.add(new LineBuffer());
            }
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delCharFromPrompt(char c, int charPointer) {
        screenPrompt.setCharacter(charPointer, 0, c);
        //System.out.print(s);
        refresh();
    }

    public static void dispose(){
        try {
            screen.clear();
            screen.close();
            screen = null;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean isClose(){
        return screen == null;
    }
}
