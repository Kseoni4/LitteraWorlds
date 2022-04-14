package org.litteraworlds.net;

import org.litteraworlds.dto.PlayerDTO;
import org.litteraworlds.objects.Player;
import org.litteraworlds.view.Debug;
import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;
import org.litteraworlds.workers.ConnectionWorker;

import java.io.IOException;
import java.util.Locale;

public class Requests {

    enum RequestHeaders {
        HASH,
        AUTH,
        PREG,
        UPDT,
        ADDZ,
        CHCK
    }

    private static ConnectionWorker connectionWorker;

    public static void setConnectionWorker(ConnectionWorker cw){
       connectionWorker = cw;
    }

    private static String convertHashToString(byte[] hash){
        String temp = "";
        for(byte b : hash){
            temp = temp.concat(String.format("%02x",b));
        }
        return temp;
    }

    public static byte[] getHash(){
        try {
            connectionWorker.sendToServer(RequestHeaders.HASH.name().toUpperCase(Locale.ROOT));
            byte[] hash = connectionWorker.getFromServer();
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
