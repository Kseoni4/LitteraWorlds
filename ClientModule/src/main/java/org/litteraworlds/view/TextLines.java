package org.litteraworlds.view;

import org.litteraworlds.objects.GameObject;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class TextLines {

    private static final int ERROR_CODE_INIT_FAILED = 1;

    private static final int SUCCESS_CODE = 0;

    private TextLines(){}

    private static HashMap<LinesType, String> linesMap = new HashMap<>();

    private static HashMap<String, String> linesPlaceholderMap = new HashMap<>();

    private static ArrayList<String> introlines = new ArrayList<>();

    public static ArrayList<String> getIntrolines(){
        return introlines;
    }

    public static String getLine(LinesType lineType){
        return linesMap.get(lineType);
    }

    public static String getLine(LinesType linesType, GameObject gameObject){
        String line = linesMap.get(linesType);
        String placeHolder = linesPlaceholderMap.get(line);
        return "%s%s".formatted(line, placeHolder.replaceAll(placeHolder, gameObject.toString()));
    }

    public static String getLine(LinesType linesType, List<? extends GameObject> gameObjects){
        String line = linesMap.get(linesType);
        String placeHolder = linesPlaceholderMap.get(line);

        StringBuilder gameObjectsString = new StringBuilder();
        for(GameObject gameObject : gameObjects){
            gameObjectsString.append(gameObject.toString()).append(" ");
        }

        return "%s%s".formatted(line, placeHolder.replaceAll(placeHolder, gameObjectsString.toString()));
    }

    public static String getLine(LinesType linesType, String value){
        String line = linesMap.get(linesType);
        String placeHolder = linesPlaceholderMap.get(line);

        return "%s%s".formatted(line, placeHolder.replaceAll(placeHolder, value));
    }

    public static int init(){
        try {

            GameScreen.putString(MessageType.SYSTEM, "Загрузка текстовых данных...");

            File linesFile = new File(TextLines.class.getResource("/textlines.csv").toURI());
            File textsFile = new File(TextLines.class.getResource("/texts.res").toURI());

            String[] lines;

            String[] placeHolders;

            try (InputStreamReader inputStream = new FileReader(linesFile)) {
                BufferedReader bufferedReader = new BufferedReader(inputStream);
                String line;

                int i = 0;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] textPlaceHolder = line.split("\\$");

                    linesMap.put(LinesType.values()[i], textPlaceHolder[0]);

                    System.out.println(LinesType.values()[i] + "->" + textPlaceHolder[0]);

                    if (textPlaceHolder.length > 1) {
                        linesPlaceholderMap.put(textPlaceHolder[0], textPlaceHolder[1]);
                        System.out.println(textPlaceHolder[0] + " as " + textPlaceHolder[1]);
                    }

                    i++;
                }
            }
            try{
                InputStreamReader is = new FileReader(textsFile);
                BufferedReader bufferedReader = new BufferedReader(is);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    introlines.add(line);
                }
            } catch (IOException e){
                e.printStackTrace();
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return ERROR_CODE_INIT_FAILED;
        }
        return SUCCESS_CODE;
    }

    private static String[] getPlaceHolders(String[] lines){
        String[] placeholders = new String[3];
        int i = 0;
        for(String line : lines){
            if(line.startsWith("%")){
                placeholders[i] = line;
                i++;
            }
        }
        return placeholders;
    }

}
