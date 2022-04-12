package org.litteraworlds.view;

public class Debug {
    private Debug(){}

    public static void toLog(String message){
        System.out.println("[DEBUG]"+message);
    }

    public static void toLog(Object object){
        toLog(object.toString());
    }
}
