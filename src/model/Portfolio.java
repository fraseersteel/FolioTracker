package model;

import java.util.*;

public class Portfolio extends Observable implements IPortfolio {

    private Map<String, Stock> stockMap;
    private String folioName;

    public Portfolio(String folioName){
        this.folioName = folioName;
        stockMap = new HashMap<>();
    }

    @Override
    public String getPortfolioName() {
        return folioName;
    }

    @Override
    public void setPortfolioName(String name) {
        this.folioName = folioName;
    }

    @Override
    public boolean removeStock(String tickerSymbol) {
        //TODO handle invalid ticker
        return stockMap.remove(tickerSymbol) != null;
    }

    public void createStock(String tickerSymbol, int numOfShares) {
        Stock newStock = new Stock(tickerSymbol, tickerSymbol + "Name", numOfShares, 1, numOfShares);//get priceper share from prices?
    }

}