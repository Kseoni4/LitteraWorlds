package org.litteraworlds.services;

import org.litteraworlds.dto.PlayerDTO;
import org.litteraworlds.map.Region;

import java.awt.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PlayerInstance implements Runnable {

    private Socket client;

    private final Logger logClient = Logger.getGlobal();

    public PlayerInstance(Socket client){
        this.client = client;
        logClient.info("Client "+client+" has connected");
    }

    @Override
    public void run() {

    }
}
