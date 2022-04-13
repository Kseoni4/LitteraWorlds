package org.litteraworlds.workers;

import org.litteraworlds.dto.PlayerDTO;
import org.litteraworlds.view.Debug;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ConnectionWorker implements Runnable {

    private Socket server = null;

    private InputStream in = null;

    private OutputStream out = null;


    public void sendToServer(PlayerDTO playerDTO) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(out);
        Debug.toLog("Отправляем "+playerDTO);
        outputStream.writeObject(playerDTO);
        outputStream.flush();
    }

    public void sendToServer(String stringRequest){
        this.sendToServer(stringRequest.getBytes(StandardCharsets.UTF_8));
    }
    public void sendToServer(byte[] outcomingData){
        try{
            BufferedOutputStream bOut = new BufferedOutputStream(out);
            String s = new String(outcomingData);

            s = s.concat("\n");

            Debug.toLog("Send to server: "+s);
            bOut.write(s.getBytes(StandardCharsets.UTF_8));
            bOut.flush();
            Debug.toLog("Flush out...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getFromServer() throws IOException {
        byte[] inputBuffer = new byte[1024];
        int l = 0;
        if((l = in.read(inputBuffer)) > 0) {
            return Arrays.copyOf(inputBuffer,l);
        } else {
            return null;
        }
    }

    public ConnectionWorker(){
        try {
            server = new Socket("localhost", 8080);

            Debug.toLog("Connection to server "+server+" is established");

            in = server.getInputStream();

            out = server.getOutputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            while(!server.isClosed()){
                TimeUnit.MILLISECONDS.sleep(500);
                /*byte[] incomingData = getFromServer();
                String inputString = "";
                if(incomingData != null){
                    inputString = new String(incomingData).trim();
                } else {
                    inputString = "nothing";
                }
                Debug.toLog("GET FROM SERVER:"+inputString);*/
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
