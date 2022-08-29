package org.litteraworlds.services;

import org.litteraworlds.services.annotations.Mapping;
import org.litteraworlds.services.annotations.Params;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * [SERVER-SIDE]
 * Класс - обработчик игровых запросов. Выполняется в отдельном потоке при каждом создании новой игровой сессии.
 * @author Николай Ксенофонтов
 * @version 1.0
 */
public class PlayerInstanceHandler implements Runnable{

    private PlayerInstance playerInstance;

    private MappingAnnotationHandler mappingAnnotationHandler;

    private BufferedInputStream bIn;

    private static HashMap<String, Method> instanceMethods = new HashMap<>();

    /*
     * При старте программы через рефлексию проходит по методам класса {@link PlayerInstance} и ищет аннотации {@link Mapping}
     * Значение каждой проверяется на соответствие префексу и в случае совпадение метод вызывается с методами, либо без них.
     */
    static {
        Class<?> playerInstanceClass = PlayerInstance.class;

        for (Method method : playerInstanceClass.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Mapping.class)){
                instanceMethods.put(method.getAnnotation(Mapping.class).value(), method);
            }
        }
    }

    public PlayerInstanceHandler(PlayerInstance playerInstance){
        this.playerInstance = playerInstance;
        this.mappingAnnotationHandler = new MappingAnnotationHandler(playerInstance);
        this.bIn = new BufferedInputStream(playerInstance.getIn());
    }

    @Override
    public void run(){
        try {
            while (!playerInstance.getClient().isClosed()) {
                byte[] inputBuffer = new byte[1024];

                byte[] incomingBytes = playerInstance.getFromClient(inputBuffer, bIn);

                System.out.println("Get incoming bytes: "+ Arrays.toString(incomingBytes));

                String request = new String(incomingBytes).trim().substring(0,4);

                byte[] requestData = Arrays.copyOfRange(incomingBytes, request.length(), incomingBytes.length);

                mappingAnnotationHandler.requestMapping(request, requestData);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * [SERVER-SIDE]
     * Обработчик аннотаций для выходных запросов
     * @author Николай Ксенофонтов
     * @version 1.0
     */
    private static class MappingAnnotationHandler {

        PlayerInstance contextInstance;

        public MappingAnnotationHandler(PlayerInstance contextInstance){
            this.contextInstance = contextInstance;
        }

        /**
         * Обработчик входных запросов.
         * Вызывает метод из текущего контекста, соответствующий входному префиксу
         * <br>
         * Список заголовочных префиксов:
         * <ul>
         *     <li>
         *         HASH - вернуть хэш строку по полученным данным
         *     <li>
         *         AUTH - аутентифициировать игрока
         *     <li>
         *         PREG - регистрация игрового персонажа
         *     <li>
         *         UPDT - обновить данные персонажа
         *     <li>
         *         CHCK - синхронизация данных клиента и сервера
         *     <li>
         *         CHNM - проверить регистрируемое имя
         *     <li>
         *         VLWD - валидировать хэш клиентского мира
         * </ul>
         * @param requestHeader заголовочный префикс
         * @param requestData передаваемые данные игрового клиента
         * @author Николай Ксенофонтов
         */
        public void requestMapping(String requestHeader, Object requestData) {
            try {

                Method m = instanceMethods.get(requestHeader);

                m.setAccessible(true);

                if(m.getParameters().length > 0){
                    m.invoke(contextInstance, requestData);
                } else {
                    m.invoke(contextInstance);
                }

            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}

