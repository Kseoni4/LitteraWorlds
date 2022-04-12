package org.litteraworlds;

import org.litteraworlds.map.Region;
import org.litteraworlds.workers.ServerListener;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class ServerStart {

    private static ServerSocket serverSocket;

    private static final Logger log = Logger.getGlobal();

    private static final int SERVER_PORT = 8080;

    private static final ExecutorService listenerService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        log.info("Starting game server on port "+SERVER_PORT);

        serverSocket = ServerSocketFactory.getDefault().createServerSocket(SERVER_PORT);

        log.info(() -> "Server socket information "+serverSocket.toString());

        new Region();
    }
}
