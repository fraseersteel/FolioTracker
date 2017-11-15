package model;

import java.util.HashMap;
import java.util.Map;

public class Prices {

    private Map<String, Double> mostRecentPrices;

    public Prices(){
        mostRecentPrices = new HashMap<>();
    }

    //make static
    //loads prices
    /*
     */

    public Double getPriceOfTicker(String ticker){
        return mostRecentPrices.get(ticker);
    }
}
