/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.wittakarn.load.balance;

import com.blogspot.wittakarn.load.balance.scheduler.HttpRequestTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wittakarn
 */
public class Sender {

    public static void main(String args[]) {
        try {

            Map<Long, URL> sortedRestURL = HttpRequestTask.sortedRestUrl;

            //loop a Map
            for (Map.Entry<Long, URL> entry : sortedRestURL.entrySet()) {
                System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());

                URL url = new URL(entry.getValue().toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    continue;
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }

                conn.disconnect();
            }

        } catch (Exception ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
