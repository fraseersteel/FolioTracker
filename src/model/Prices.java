package model;

import model.QuoteServer.NoSuchTickerException;
import model.QuoteServer.StrathQuoteServer;
import model.QuoteServer.WebsiteDataException;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Prices extends Observable{

    private static Map<String, Double> mostRecentPrices = new HashMap<>();

    public Prices(){
    }

    public static boolean addTicker(String ticker){
        if(!mostRecentPrices.containsKey(ticker)){
//            String str;
//            try {
//                str = StrathQuoteServer.getLastValue(ticker);
//            } catch (Exception e) {
//                return false;
//            }
////            System.out.println("found: " + str);
//            Double dbl = Double.parseDouble(str);
//            System.out.println("Double: " + dbl);
            mostRecentPrices.put(ticker, 0.0);
            return true;
        }
        return false;
    }

//    public static boolean removeTicker(String ticker){
//        if(mostRecentPrices.containsKey(ticker)){
//            mostRecentPrices.remove(ticker);
//        }
//        return false;
//    }

    public static Double getPriceOfTicker(String ticker){
        if(mostRecentPrices.containsKey(ticker)){
            return mostRecentPrices.get(ticker);
        }
        return null;
    }

    public void refresh(){
        for(String ticker : mostRecentPrices.keySet()){
            try {
                String str = StrathQuoteServer.getLastValue(ticker);
                Double dbl = Double.parseDouble(str);
                mostRecentPrices.put(ticker, dbl);
            } catch (Exception e) {}
        }
        setChanged();
        notifyObservers(ViewUpdateType.PRICE);
    }
}
