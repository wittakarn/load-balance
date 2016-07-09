/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.wittakarn.load.balance.scheduler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wittakarn
 */
public class HttpRequestTask extends TimerTask {

    public static List<URL> restApiUrls = new ArrayList<URL>();

    public static Map<Long, URL> sortedRestUrl = new TreeMap<Long, URL>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o1.compareTo(o2);
        }
    });

    private static Timer timer = new Timer();

    @Override
    public void run() {
        try {
            int delay = randomTimeForScheduling();
            
            System.out.println("delay : = " + delay);

            timer.schedule(new HttpRequestTask(), delay);

            restApiUrls.clear();
            sortedRestUrl.clear();

            //This method applied to read url form file, which can support code changes without add the new servers for Rest API
            readRestApiUrlsFromFileAndAddToList();

            Map<Long, URL> unsortedRestUrl = sendHttpRequest();

            sortedRestUrl.putAll(unsortedRestUrl);
            
            printMap();
        } catch (IOException ex) {
            Logger.getLogger(HttpRequestTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<Long, URL> sendHttpRequest() throws IOException {
        HttpURLConnection conn = null;
        URL currentUrl = null;
        Map<Long, URL> unsortedRestUrl = new HashMap<Long, URL>();

        for (int i = 0; i < restApiUrls.size(); i++) {

            currentUrl = restApiUrls.get(i);

            conn = (HttpURLConnection) restApiUrls.get(i).openConnection();
            long starTime = System.currentTimeMillis();
            conn.connect();

            if (conn.getResponseCode() < 500) {

                long elasedTime = System.currentTimeMillis() - starTime;
                System.out.println(elasedTime);

                unsortedRestUrl.put(elasedTime, currentUrl);

            }
            conn.disconnect();
        }
        
        return unsortedRestUrl;
    }

    private static int randomTimeForScheduling() {
        Random r = new Random();
        //random delay time from 1 second to 3 minute.
        return r.nextInt(180000) + 1000;
    }

    private void readRestApiUrlsFromFileAndAddToList() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:/rest-url.txt"));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            System.out.println(sCurrentLine);
            restApiUrls.add(new URL(sCurrentLine));
        }
    }

    private static void printMap() {
        for (Map.Entry<Long, URL> entry : sortedRestUrl.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }
}
