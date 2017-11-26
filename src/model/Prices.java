package model;

import QuoteServer.NoSuchTickerException;
import QuoteServer.QuoteServer;
import QuoteServer.StrathQuoteServer;
import QuoteServer.WebsiteDataException;
import com.sun.org.apache.xpath.internal.operations.Quo;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Prices extends Observable implements Serializable{

    private static final long serialVersionUID = 64L;

    private static Map<String, Double> mostRecentPrices = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();
    private static QuoteServer quoteServer;

    public Prices(QuoteServer quoteServer){
        this.quoteServer = quoteServer;
    }

    public Prices(QuoteServer quoteServer, Map<String, Double> mostRecentPrices){
        this.mostRecentPrices.putAll(mostRecentPrices);
        this.quoteServer = quoteServer;
    }

    public static boolean addTicker(String ticker){
        ticker = ticker.toUpperCase();
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
        ticker = ticker.toUpperCase();
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
        System.out.println("notifying");
        notifyObservers(ViewUpdateType.STOCKPRICE);
    }

    private static void readAndSet(String ticker) throws NoSuchTickerException, WebsiteDataException {
       lock.lock();
        try{
            System.out.println();
            System.out.println("Updating: [" + ticker+ "]");
            String str = quoteServer.getLastValue(ticker);
            Double dbl = Double.parseDouble(str);
            System.out.println("Price before update:" + mostRecentPrices.get(ticker) + " setting to:" + dbl);
            mostRecentPrices.put(ticker, dbl);
            System.out.println("Price after update:" + mostRecentPrices.get(ticker));
            System.out.println();
        }finally {
            lock.unlock();
        }
    }
}
