package org.litteraworlds.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;
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

    public static byte[] getHash(String data){
        if(data.equals("")) {
            data = new Random().ints().mapToObj(Character::toString).collect(Collectors.joining());
        }
        try {
            hashGen = MessageDigest.getInstance("SHA-256");

            byte[] rawHash = hashGen.digest(data.getBytes(StandardCharsets.UTF_8));

            String encodedHash = "";

            for(byte b : rawHash){
                encodedHash = encodedHash.concat(String.format("%02x",b));
            }

            System.out.println("Generate new hash: "+encodedHash);

            //Logger.getGlobal().info("Generate new hash: "+encodedHash);

            //Logger.getGlobal().info("Hash byte array:" + Arrays.toString(rawHash));
            //Logger.getGlobal().info("Encoded byte array:" + Arrays.toString(encodedHash.getBytes()));

            return rawHash;

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            //Logger.getGlobal().info("Hash algorithm is not found, return input data");
            return data.getBytes();
        }
    }
}
