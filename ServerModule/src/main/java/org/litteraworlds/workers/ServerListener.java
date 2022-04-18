package org.litteraworlds.workers;

import org.litteraworlds.services.PlayerInstance;
import org.litteraworlds.services.PlayerInstanceHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class ServerListener implements Runnable {

    private final int ERROR_CODE = 1;

    private final int SUCCESS_EXIT = 0;

    private final ServerSocket serverSocket;

    private final Logger log = Logger.getGlobal();

    private final ExecutorService playerInstancesWorkers = Executors.newCachedThreadPool();

    private final ArrayList<PlayerInstance> instances = new ArrayList<>();

    public ServerListener(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void shutdown() throws IOException {

    }

    @Override
    public void run() {
        try {
            Socket client;
            while ((client = serverSocket.accept()) != null) {
                PlayerInstance playerInstance = new PlayerInstance(client);

                PlayerInstanceHandler playerInstanceHandler = new PlayerInstanceHandler(playerInstance);

                log.info("Player with net-info: "+client+" has connected");

                instances.add(playerInstance);

                playerInstancesWorkers.execute(playerInstanceHandler);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
