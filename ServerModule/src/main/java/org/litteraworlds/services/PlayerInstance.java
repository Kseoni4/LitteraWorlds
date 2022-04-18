package org.litteraworlds.services;

import org.litteraworlds.dto.PlayerDTO;
import org.litteraworlds.security.HashGen;
import org.litteraworlds.services.annotations.Mapping;
import org.litteraworlds.services.annotations.Params;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * <h2>[SERVER-SIDE]</h2>
 * <h3>PlayerInstance</h3>
 * Экземпляр сессии пользователя.
 * Содержит методы, которые выполняют различные запросы от клиента. Методы аннотируются интерфейсом {@link Mapping} и вызываются через рефлексию в классе-обработчике
 * {@link PlayerInstanceHandler}.
 */
public class PlayerInstance {

    public static final String WAIT_RESPONSE = "WAIT_FOR_DTO";

    private Socket client;

    private final Logger logClient = Logger.getGlobal();

    private InputStream in;

    private OutputStream out;

    private String clIP;

    public Socket getClient() {
        return client;
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public PlayerInstance(Socket client) throws IOException {
        this.client = client;
        this.in = client.getInputStream();
        this.out = client.getOutputStream();
        logClient.info("Client " + client + " has connected");
    }

    private byte[] trimBuffer(byte[] buffer){

        int size = 0;

        for(int i = 0; i < buffer.length; i++){
            if(buffer[i] == 0){
                break;
            }
            size++;
        }
        return Arrays.copyOfRange(buffer, 0, size);
    }

    public byte[] getFromClient(byte[] buffer, BufferedInputStream bIn) throws IOException {
        if(bIn.read(buffer) > 0){
            return trimBuffer(buffer);
        } else {
            return new byte[0];
        }
    }

    public void sendToClient(byte[] reply) {
        logClient.info(clIP+"|>Reply: " + Arrays.toString(reply));
        try {
            out.write(reply);
            out.flush();
        } catch (IOException e) {
            logClient.info("|>Error");
        }
    }

    @Mapping("HASH")
    private void sendHash(@Params byte[] data){

        String stringData = new String(data);

        logClient.info(clIP+"|> get data: "+stringData);

        byte[] hashResponse = HashGen.getHash("Genesis");

        logClient.info(clIP+"|> send hash: "+stringData);

        sendToClient(hashResponse);
    }

    //TODO: при регистрации игрока, помимо ответа "OK" должна возвращаться
    // сгенерированная строка для поля tokenID. Необходимо добавить локальное данных о нём на диск
    // и в файловую "базу данных" типа csv, в формате:
    // tokenID, playerName, playerPlaceHashID (это хэш местоположения)
    /**
     * Обработчик регистрации нового игрока. Готовность о приёме данных сообщается ответным запросом:
     * {@value #WAIT_RESPONSE}, после чего входной поток оборачивается классом {@link ObjectInputStream} и готовится принимать
     * сериализованные данные класса {@link PlayerDTO}, см.@see <a href="https://ru.wikipedia.org/wiki/DTO">Data Transfer Object</a>
     */

    @Mapping("PREG")
    private void registerNewPlayer(){
        try {

            sendToClient(WAIT_RESPONSE.getBytes(StandardCharsets.UTF_8));

            ObjectInputStream inputStream = new ObjectInputStream(in);

            PlayerDTO playerDTO = (PlayerDTO) inputStream.readObject();

            logClient.info(clIP + "|> player data: "+playerDTO.toString());

            logClient.info(clIP + "|> send response OK");

            sendToClient("OK".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e){
            logClient.info(clIP + "|> send response NOT OK");
            sendToClient("NOT OK".getBytes(StandardCharsets.UTF_8));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Mapping("VLWD")
    private void validateWorld(@Params byte[] worldHash){
        logClient.info("Get world hash bytes for validate");
        logClient.info(Arrays.toString(worldHash));

        String worldHashLine = "";

        for(byte b : worldHash){
            worldHashLine = worldHashLine.concat(String.format("%02x",b));
        }

        logClient.info("World hash line ");
        logClient.info(worldHashLine);

        if(Arrays.equals(HashGen.getHash("Genesis"), worldHash)){
            logClient.info("World hash id is valid");
            sendToClient("VALID".getBytes(StandardCharsets.UTF_8));
        } else {
            logClient.info("World hash id is not valid");
            sendToClient("NOT_VALID".getBytes(StandardCharsets.UTF_8));
        }
    }
}
