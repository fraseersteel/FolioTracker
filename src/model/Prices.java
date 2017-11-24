package model;

import model.QuoteServer.NoSuchTickerException;
import model.QuoteServer.StrathQuoteServer;
import model.QuoteServer.WebsiteDataException;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Prices extends Observable implements Serializable{

    private static final long serialVersionUID = 64L;

    private static Map<String, Double> mostRecentPrices = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();

    public Prices(){}

    public Prices(Map<String, Double> mostRecentPrices){
        this.mostRecentPrices.putAll(mostRecentPrices);
    }

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

    public Map<String, Double> getCurrentPriceList(){
        Map<String, Double> encapsMap = new HashMap<>();
        for(String key : mostRecentPrices.keySet()){
            encapsMap.put(new String(key), new Double(mostRecentPrices.get(key)));
        }
        return encapsMap;
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
