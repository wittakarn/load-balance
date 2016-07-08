/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.wittakarn.load.balance.scheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wittakarn
 */
public class HttpRequestTask extends TimerTask {

    public static List<URL> restApiUrls = new ArrayList<URL>();
    public static URL bestRestUrl;
    private static Timer timer = new Timer();

    @Override
    public void run() {
        try {
            int delay = randomTimeForScheduling();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.print(dateFormat.format(date));
            System.out.println(" delay : = " + delay);
            timer.schedule(new HttpRequestTask(), delay);
            readRestApiUrlsFromFileAndAddToList();
            sendHttpRequest();
            
            System.out.println("Best rest url : = " + bestRestUrl);
        } catch (IOException ex) {
            Logger.getLogger(HttpRequestTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendHttpRequest() throws IOException {
        HttpURLConnection conn = null;
        Long minElasedTime = null;
        URL currentUrl = null;
        for (int i = 0; i < restApiUrls.size(); i++) {
            
            currentUrl = restApiUrls.get(i);
            
            conn = (HttpURLConnection) restApiUrls.get(i).openConnection();
            long starTime = System.currentTimeMillis();
            conn.connect();
            
            if(conn.getResponseCode() < 500){

                long elasedTime = System.currentTimeMillis() - starTime;
                System.out.println(elasedTime);

                if(minElasedTime == null){
                    minElasedTime = elasedTime;
                }

                if(elasedTime < minElasedTime){
                    bestRestUrl = currentUrl;
                    minElasedTime = elasedTime;
                }
            
            }

            conn.disconnect();
        }
        restApiUrls.clear();
    }

    private static int randomTimeForScheduling() {
        Random r = new Random();
        return r.nextInt(180000) + 1000;
    }

    private void readRestApiUrlsFromFileAndAddToList() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("B:/url.txt"));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            System.out.println(sCurrentLine);
            restApiUrls.add(new URL(sCurrentLine));
        };
    }
}
