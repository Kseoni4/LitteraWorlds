package org.litteraworlds.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.stream.Collectors;

public class HashGen {
    private HashGen() {}

    private static MessageDigest hashGen;

    private static String encodeToString(byte[] rawData){
        StringBuilder sb = new StringBuilder();
        for (byte rawDatum : rawData) {
            if ((0xff & rawDatum) < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(0xff & rawDatum));
        }
        return sb.toString();
    }

    public static byte[] getHash(byte[] data){
        try {
            hashGen = MessageDigest.getInstance("SHA-256");

            byte[] rawHash = hashGen.digest(data);

            String encodedHash = HashToString.convert(rawHash);

            System.out.println("Generate new hash: "+encodedHash);
            return rawHash;

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return data;
        }
    }

    public static byte[] getHash(String data){
        if(data.equals("")) {
            data = new Random().ints().mapToObj(Character::toString).collect(Collectors.joining());
        }
        try {
            hashGen = MessageDigest.getInstance("SHA-256");

            byte[] rawHash = hashGen.digest(data.getBytes(StandardCharsets.UTF_8));

            String encodedHash = HashToString.convert(rawHash);

            System.out.println("Generate new hash: "+encodedHash);
            return rawHash;

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return data.getBytes();
        }
    }
}
