package model;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Portfolio extends Observable implements IPortfolio, Serializable {

    private static final long serialVersionUID = 54L;

    private Map<String, Stock> stockMap;
    private String folioName;

    public Portfolio(String folioName){
        this.folioName = folioName;
        stockMap = new HashMap<>();
    }

    public Portfolio(IPortfolio portfolio){
        this.folioName = portfolio.getPortfolioName();
        stockMap = new HashMap<>();
        for(String s : getStockTickers()){
            stockMap.put(s, (Stock) portfolio.getStockByTicker(s));
        }
    }

    @Override
    public String getPortfolioName() {
        return new String(folioName);
    }

    @Override
    public Set<String> getStockTickers() {
        return stockMap.keySet();
    }

    @Override
    public IStock getStockByTicker(String name) {
        return stockMap.get(name);
    }

    public Boolean sellStock(String tickerSymbol, int numOfShares){
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            if(numOfShares < stock.getNumShares()){
                stock.sellShares(numOfShares);
                setChanged();
                notifyObservers(ViewUpdateType.NUMBEROFSHARES);
            }else if(numOfShares == stock.getNumShares()){
                stockMap.remove(ticker);
                setChanged();
                notifyObservers(ViewUpdateType.DELETION);
            }else{
                return null;
            }
            return true;
        }
        return false;
    }

    public Boolean buyStock(String tickerSymbol, int numOfShares){
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            stock.buyShares(numOfShares);
            setChanged();
            notifyObservers(ViewUpdateType.NUMBEROFSHARES);
            return true;
        }
        return createStock(ticker, numOfShares);
    }

    private Boolean createStock(String tickerSymbol, int numOfShares) {
        String ticker = tickerSymbol.toUpperCase();
        if(Prices.addTicker(ticker)){
            Stock newStock = new Stock(ticker, ticker + "Name", numOfShares);
            stockMap.put(ticker,newStock);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            return true;
        }
        return false;
    }
}