package org.litteraworlds.workers;

import org.litteraworlds.view.GameScreen;
import org.litteraworlds.view.MessageType;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ConnectionWorker implements Runnable {

    private static Socket server;

    public static boolean isAlive(){
        return server.isConnected();
    }

    @Override
    public void run() {

    }
}
