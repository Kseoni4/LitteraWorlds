package org.litteraworlds.input;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.litteraworlds.view.GameScreen;

import java.io.IOException;

public final class PlayerInput {

    private static KeyStroke key;

    private static char[] charBuffer;

    private static final int charBufferSize = 16;

    private static final int DEFAULT_POINTER = 1;

    public static String inputCommand() {

        int pointer = DEFAULT_POINTER;

        key = null;

        charBuffer = new char[16];

        try {
            while (commandNotEnter(GameScreen.getScreenInput())) {
                if (keyIsValid()) {

                    if (key.getKeyType().equals(KeyType.Backspace) && pointer > 1) {

                        System.out.println(charBuffer[pointer - 1] + " is deleted");

                        charBuffer[--pointer] = ' ';

                        GameScreen.delCharFromPrompt(charBuffer[pointer], pointer);

                        clearKey();

                        continue;
                    }

                    if (pointer < charBuffer.length) {

                        charBuffer[pointer] = clearInput(key.getCharacter());

                        GameScreen.putCharIntoPrompt(charBuffer[pointer], pointer++);
                    }
                }
                clearKey();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        clearKey();

        String line = new String(charBuffer).trim();

        if (!line.equals("")) {
            GameScreen.putString(line);
        }

        GameScreen.resetAndClearPrompt();

        return line;
    }

    private static boolean keyIsValid() {
        return (key != null);
    }

    private static void clearKey(){
        key = null;
    }

    private static boolean commandNotEnter(KeyStroke keyStroke){
        if(keyStroke != null){
            key = keyStroke;
            return !key.getKeyType().equals(KeyType.Enter);
        } else {
            return true;
        }
    }

    private static char clearInput(char c){
        if(Character.isLetterOrDigit(c) || Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '/'){
            return c;
        } else {
            return ' ';
        }
    }

    private PlayerInput(){}
}
