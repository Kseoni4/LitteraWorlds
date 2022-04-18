package org.litteraworlds.services;

import org.litteraworlds.services.annotations.Mapping;
import org.litteraworlds.services.annotations.Params;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class PlayerInstanceHandler implements Runnable{

    PlayerInstance playerInstance;

    MappingAnnotationHandler mappingAnnotationHandler;

    BufferedInputStream bIn;

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

    private static class MappingAnnotationHandler {

        PlayerInstance contextInstance;

        public MappingAnnotationHandler(PlayerInstance contextInstance){
            this.contextInstance = contextInstance;
        }

        /**
         * Обработчик входных запросов.
         * Вызывает метод из текущего контекста, соответствующий входному префиксу
         * Через рефлексию проходит по методам класса {@link PlayerInstance} и ищет аннотации {@link Mapping}
         * Значение каждой проверяется на соответствие префексу и в случае совпадение метод вызывается с методами, либо без них.
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
         */
        public void requestMapping(String requestHeader, Object requestData) {
            try {
                Class<?> pist = PlayerInstance.class;

                for (Method method : pist.getDeclaredMethods()) {

                    Mapping mapping = method.getAnnotation(Mapping.class);

                    if (mapping != null && mapping.value().equals(requestHeader)) {

                        if (method.getParameters().length > 0) {

                            for (Parameter p : method.getParameters()) {

                                Params params = p.getAnnotation(Params.class);

                                if (params != null) {
                                    method.setAccessible(true);
                                    method.invoke(contextInstance, requestData);
                                }
                            }
                        } else {
                            method.setAccessible(true);
                            method.invoke(contextInstance);
                        }
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}

