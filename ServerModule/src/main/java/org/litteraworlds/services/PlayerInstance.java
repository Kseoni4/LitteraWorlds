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
 * <br>Асинхронно обрабатывает запросы, приходящие из соответствующего клиентского сокета
 * Запросы перенаправляются при помощи метода {@linkplain  #requestMapping(String, String)},
 * принимающего префикс запроса и входные данные
 */
public class PlayerInstance implements Runnable {

    public static final String WAIT_RESPONSE = "WAIT_FOR_DTO";

    private Socket client;

    private final Logger logClient = Logger.getGlobal();

    private InputStream in;

    private OutputStream out;

    private String clIP;

    public PlayerInstance(Socket client) throws IOException {
        this.client = client;
        this.in = client.getInputStream();
        this.out = client.getOutputStream();
        logClient.info("Client " + client + " has connected");
    }

    @Override
    public void run() {
        logClient.info("Start player instance");
        logClient.info("Player info " + client);
        try {
            BufferedInputStream bIn = new BufferedInputStream(in);

            clIP = client.getInetAddress().getHostAddress() + ":" + client.getPort();

            logClient.info(clIP + "|>Do work...");

            while (!client.isClosed()) {
                logClient.info(clIP + "|>Wait for read");

                byte[] inputBuffer = new byte[1024];

                if ((bIn.read(inputBuffer)) > 0) {
                    String request = new String(inputBuffer).trim();

                    logClient.info(clIP+"|> get request: "+request);

                    String requestData = "";

                    if(request.length() > 4){
                        requestData = request.substring(4);
                    }
                    requestMapping(request.substring(0, 4), requestData);
                } else {
                    logClient.info(clIP+"|> get nothing...");
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обработчик входных запросов. Перенаправляет контекст вызова в одну из функций соответствующиему входному префиксу.
     * <br>
     * Список заголовочных префиксов:
     * <lu>
     *     <li>
     *         HASH - вернуть хэш строку по полученным данным
     *     </li>
     *     <li>
     *         AUTH - аутентифициировать игрока
     *     </li>
     *     <li>
     *         PREG - регистрация игрового персонажа
     *     </li>
     *     <li>
     *         UPDT - обновить данные персонажа
     *     </li>
     *     <li>
     *         CHCK - синхронизация данных клиента и сервера
     *     </li>
     *     <li>
     *         CHNM - проверить регистрируемое имя
     *     </li>
     *
     * </lu>
     * @param prefix
     * @param data
     */
    private void requestMapping(String prefix, String data) {
        switch (prefix) {
            case "HASH" -> {
                sendHash(data);
            }
            case "AUTH" -> {
                return;
            }
            case "PREG" -> {
                registerNewPlayer();
            }
            case "UPDT" -> {
                return;
            }
            case "CHCK" -> {
                return ;
            }
            case "CHNM" -> {
                return;
            }

            default -> logClient.info(clIP+"|> get unknown prefix "+prefix);
        }
    }

    private void sendToClient(byte[] reply) {
        logClient.info(clIP+"|>Reply: " + Arrays.toString(reply));
        try {
            out.write(reply);
            out.flush();
        } catch (IOException e) {
            logClient.info("|>Error");
        }
    }

    @Mapping("HASH")
    private void sendHash(@Params String data){
        logClient.info(clIP+"|> get data: "+data);

        byte[] hashResponse = HashGen.getHash("Genesis");

        logClient.info(clIP+"|> send hash: "+data);

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
}
