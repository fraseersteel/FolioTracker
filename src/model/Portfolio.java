package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Portfolio extends Observable implements IPortfolio {

    private List<Stock> portfolio = new ArrayList<Stock>();


    public void createStock(String tickerSymbol, int numOfShares) {
    //TODO implement
        //notify 
    }


}