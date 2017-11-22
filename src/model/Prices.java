package model;

import model.QuoteServer.NoSuchTickerException;
import model.QuoteServer.StrathQuoteServer;
import model.QuoteServer.WebsiteDataException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Prices extends Observable{

    private static Map<String, Double> mostRecentPrices = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();

    public Prices(){}

    public static boolean addTicker(String ticker){
        try {
            if(!mostRecentPrices.containsKey(ticker)){
                readAndSet(ticker);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Double getPriceOfTicker(String ticker){
        if(mostRecentPrices.containsKey(ticker)){
            return mostRecentPrices.get(ticker);
        }
        return null;
    }

    public void refresh(){
        for(String ticker : mostRecentPrices.keySet()){
            try {
                readAndSet(ticker);
            } catch (Exception e) {}
        }
        setChanged();
        notifyObservers(ViewUpdateType.STOCKPRICE);
    }

    private static void readAndSet(String ticker) throws NoSuchTickerException, WebsiteDataException {
       lock.lock();
        try{
            String str = StrathQuoteServer.getLastValue(ticker);
            Double dbl = Double.parseDouble(str);
            mostRecentPrices.put(ticker, dbl);
        }finally {
            lock.unlock();
        }
    }
}
