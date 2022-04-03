package org.litteraworlds.input;

import org.litteraworlds.game.GameLogic;
import org.litteraworlds.view.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class Command {
    private static String leftOperand;
    private static String rightOperand;
    private static String command;

    private static HashMap<String, Method> commands;

    public static void init(){
        commands = new HashMap<>();
        try {
            InputStream is = Command.class.getResource("/commandlist.ctl").openStream();

            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);

            String[] lines = new String(bufferedInputStream.readAllBytes()).split("\n");

            for(String line : lines){

                String commandLocale = line.split(",")[0];

                String methodName = line.split(",")[1];

                if(commandLocale.split(" ").length > 1) {
                    String[] commandWithOp = commandLocale.split(" ");

                    commandLocale = commandWithOp[0];

                    char operandType = commandWithOp[1].toCharArray()[0];

                    commands.put(commandLocale.concat(""+(commandWithOp.length-1)), GameLogic.class.getDeclaredMethod(methodName, getClassFromType(operandType)));

                    continue;
                }

                commands.put(commandLocale, GameLogic.class.getDeclaredMethod(methodName));
            }

            bufferedInputStream.close();
            is.close();
        } catch (IOException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getClassFromType(char type){
        switch (type){
            case 'S' -> {
                return String.class;
            }
            default -> {
                return Class.class;
            }
        }
    }

    public static Map<String, Method> getCommandList(){
        return commands;
    }

    public static void parse(String buffer) {
        String[] commandLine = buffer.split(" ");

        command = commandLine[0].substring(1);

        if(commandLine.length > 1) {
            command = command.concat("1");
            rightOperand = commandLine[1];
        }

        if(commandLine.length > 2) {
            command = command.concat("2");
            rightOperand = commandLine[2];
        }

        execute(command, rightOperand, leftOperand);
    }

    private static void execute(String command, String... operands) {
        try {
            switch (getNumberOperands(operands)){
                case 1  -> commands.get(command).invoke(GameLogic.class, operands[0]);
                case 2  -> commands.get(command).invoke(GameLogic.class, operands[0], operands[1]);
                default -> commands.get(command).invoke(GameLogic.class);
            }
        } catch (InvocationTargetException | IllegalAccessException | NullPointerException e) {
            GameScreen.putString(MessageType.ERROR, TextLines.getLine(LinesType.UNKNOWN_COMMAND));
        }
    }

    private static int getNumberOperands(String... operands){
        int count = 0;
        for(Object o : operands){
            if(o != null){
                count++;
            }
        }
        return count;
    }
}
