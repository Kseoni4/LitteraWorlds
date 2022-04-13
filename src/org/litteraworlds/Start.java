package org.litteraworlds;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Start {

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                ServerStart.main(new String[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> ClientStart.main(new String[0])).start();
        System.out.println("End of start");
    }
}
