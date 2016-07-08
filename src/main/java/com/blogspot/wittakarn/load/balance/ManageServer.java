/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blogspot.wittakarn.load.balance;

import com.blogspot.wittakarn.load.balance.scheduler.HttpRequestTask;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Wittakarn
 */
public class ManageServer {

    public static void main(String args[]) {
        try {
            System.out.println(HttpRequestTask.restApiUrls.size());
            
            HttpRequestTask.restApiUrls.add(new URL("http://taobaoservicecargo.com"));
            HttpRequestTask.restApiUrls.add(new URL("http://se.bigone-electric.com/SEQUOT"));
            
            System.out.println(HttpRequestTask.restApiUrls.size());
        } catch (MalformedURLException ex) {
            Logger.getLogger(ManageServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
