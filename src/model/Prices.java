package model;

import model.QuoteServer.NoSuchTickerException;
import model.QuoteServer.StrathQuoteServer;
import model.QuoteServer.WebsiteDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prices {

    private Map<String, Double> mostRecentPrices;

    private List<String> namesToKnowAbout;

    public Prices(){
        mostRecentPrices = new HashMap<>();
        namesToKnowAbout = new ArrayList<>();
    }

    public Double getPriceOfTicker(String ticker){
        return mostRecentPrices.get(ticker);
    }

    public void refresh(){
        for(String ticker : namesToKnowAbout){
            try {
                String str = StrathQuoteServer.getLastValue(ticker);
            } catch (Exception e) {}
        }
    }
}
