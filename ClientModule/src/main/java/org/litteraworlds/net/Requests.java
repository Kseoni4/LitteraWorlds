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
        VLWD;

        public String getUpperCaseName(){
            return this.name().toUpperCase(Locale.ROOT);
        }
    }

    private static final String WAIT_DTO_RESPONSE = "WAIT_FOR_DTO";
    private static final String GOOD_RESPONSE = "OK";
    private static final String BAD_RESPONSE = "NOT OK";

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

            //Формируем пакет данных для отправки
            //Формат такой:
            //От 0 до 3 байта - заголовок, остальное - полезные данные.
            byte[] reqPack = createRequestPackage(RequestHeaders.VLWD.getUpperCaseName().getBytes(StandardCharsets.UTF_8), worldHash);

            connectionWorker.sendToServer(reqPack);

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
            connectionWorker.sendToServer(RequestHeaders.HASH.getUpperCaseName());
            byte[] hash = connectionWorker.getFromServer();
            Debug.toLog("Получен хэш: "+ Arrays.toString(hash));
            return hash;
        } catch (IOException e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * <h3>[CLIENT-SIDE]</h3>
     * <h4>Регистрация игрока на сервере</h4>
     * <p>
     *   Функция принимает полученный на этапе создания персонажа объект {@link Player} и пакует его в DTO контейнер
     *   ({@link PlayerDTO} для сериализации в поток байт и передачи на сервер.<br>
     *   После отправки данных ождиает ответ. Если ответ от сервера {@value #GOOD_RESPONSE}, то игрок успешно зарегистрирован и вернуть true.<br>
     *   В противном случае либо вызвать {@link IOException}, либо вернуть false.
     * </p>
     * @param player ссылка на игрового персонажа
     * @return ture или false в зависимости от успеха регистрации
     */
    public static boolean registerPlayer(Player player){
        try {
            connectionWorker.sendToServer(RequestHeaders.PREG.getUpperCaseName());

            String response = new String(connectionWorker.getFromServer());
            Debug.toLog("Response: "+response);

            if(response.equals(WAIT_DTO_RESPONSE)) {
                connectionWorker.sendToServer(new PlayerDTO(player));

                response = new String(connectionWorker.getFromServer());

                return response.equals(GOOD_RESPONSE);
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
