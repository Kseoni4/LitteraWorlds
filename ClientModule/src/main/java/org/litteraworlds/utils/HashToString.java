package org.litteraworlds.utils;

public class HashToString {
    /**
     * Конвертация байтового хэша в строку
     * @param hashBytes массив байтов (хэш)
     * @return хэш в виде строки
     */
    public static String convert(byte[] hashBytes){
        String h = "";
        for(byte b : hashBytes){
            h = h.concat(String.format("%02x",b));
        }
        return h;
    }
}
