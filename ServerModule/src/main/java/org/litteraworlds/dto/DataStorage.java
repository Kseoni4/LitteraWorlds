package org.litteraworlds.dto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class DataStorage {

    private DataStorage(){}

    private static final ArrayList<PlayerDTO> dtoCache = new ArrayList<>();

    private static Semaphore semaphore = new Semaphore(1);

    public static void saveData(PlayerDTO playerDTO){
        try {
            semaphore.acquire();

            dtoCache.add(playerDTO);
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public static void flushDataToFile(){
        FileOutputStream outputStream;
        ObjectOutputStream stream;

        for(PlayerDTO playerDTO : dtoCache){
            //outputStream = new FileOutputStream(new File(""));
        }
    }


}
