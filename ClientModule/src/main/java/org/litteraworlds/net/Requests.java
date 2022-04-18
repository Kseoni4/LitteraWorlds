package org.litteraworlds.net;

import org.litteraworlds.dto.PlayerDTO;
import org.litteraworlds.objects.Player;
import org.litteraworlds.view.Debug;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;
import org.litteraworlds.workers.ConnectionWorker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;

public class Requests {

    enum RequestHeaders {
        HASH,
        AUTH,
        PREG,
        UPDT,
        ADDZ,
        CHCK,
        VLWD
    }

    private static ConnectionWorker connectionWorker;

    public static void setConnectionWorker(ConnectionWorker cw){
       connectionWorker = cw;
    }

    private static byte[] createRequestPackage(String header, String data){
        return createRequestPackage(header.getBytes(StandardCharsets.UTF_8), data.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] createRequestPackage(byte[] header, byte[] data){
        byte[] outcomeRequest = new byte[header.length+data.length];

        System.arraycopy(header,0, outcomeRequest, 0, header.length);
        System.arraycopy(data, 0, outcomeRequest, header.length, data.length);

        return outcomeRequest;
    }

    private static String convertHashToString(byte[] hash){
        String temp = "";
        for(byte b : hash){
            temp = temp.concat(String.format("%02x",b));
        }
        return temp;
    }

    public static boolean validateWorld(byte[] worldHash){
        try {
            connectionWorker.sendToServer(createRequestPackage(RequestHeaders.VLWD.name().toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8), worldHash));

            byte[] response = connectionWorker.getFromServer();

            if(new String(response).equals("VALID")){
                return true;
            } else {
                throw new IOException();
            }

        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] getHash(){
        try {
            connectionWorker.sendToServer(RequestHeaders.HASH.name().toUpperCase(Locale.ROOT));
            byte[] hash = connectionWorker.getFromServer();
            Debug.toLog("Получен хэш: "+ Arrays.toString(hash));
            return hash;
        } catch (IOException e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static boolean registerPlayer(Player player){
        try {
            String response = "";
            connectionWorker.sendToServer(RequestHeaders.PREG.name().toUpperCase(Locale.ROOT));

            response = new String(connectionWorker.getFromServer());
            Debug.toLog("Response: "+response);
            if(response.equals("WAIT_FOR_DTO")) {
                connectionWorker.sendToServer(new PlayerDTO(player));

                response = new String(connectionWorker.getFromServer());

                if (response.equals("OK")) {
                    return true;
                } else {
                    throw new IOException();
                }
            } else {
                throw new IOException();
            }
        } catch (IOException e){
            GameScreen.putString(MessageType.ERROR,"При отправке данных произошла ошибка:");
            GameScreen.putString(MessageType.ERROR,e.getMessage());
            return false;
        }
    }

}
