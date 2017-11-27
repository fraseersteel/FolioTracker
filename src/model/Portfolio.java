package model;

import javax.sound.sampled.Port;
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

    @Override
    public String getPortfolioName() {
        return new String(folioName);
    }

    @Override
    public Set<String> getStockTickers() {
        Set<String> tickersCopy = new HashSet<>();
        tickersCopy.addAll(stockMap.keySet());
        return tickersCopy;
    }

    @Override
    public IStock getStockByTicker(String name) {
        return new Stock(stockMap.get(name));
    }

    @Override
    public Boolean setNameOfStock(String ticker, String newName) {
        if(stockMap.containsKey(ticker)){
            stockMap.get(ticker).setStockName(newName);
            return true;
        }
        return false;
    }

    public Boolean sellStock(String tickerSymbol, int numOfShares){
        Integer amountBefore = null;

        Boolean isSuccessful = null;
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            assert (amountBefore = stock.getNumShares()) != null;
            if(numOfShares < stock.getNumShares()){
                stock.sellShares(numOfShares);
                setChanged();
                notifyObservers(ViewUpdateType.NUMBEROFSHARES);
                isSuccessful = true;
            }else if(numOfShares == stock.getNumShares()){
                stockMap.remove(ticker);
                setChanged();
                notifyObservers(ViewUpdateType.DELETION);
                isSuccessful = true;
            }else{
                isSuccessful = false;
            }
            assert checkSell(tickerSymbol, amountBefore, numOfShares): "amountBefore:" + amountBefore + " amountToSell:" + numOfShares;
        }
        return isSuccessful;
    }

    private boolean checkSell(String ticker, int amountBefore, int amountToRemove){
        if(stockMap.containsKey(ticker)){
            int amountNow = stockMap.get(ticker).getNumShares();
            System.out.println("amountNow:" + amountNow);
            return (amountNow == (amountBefore-amountToRemove) && amountNow > 0) || (amountNow == amountBefore && amountBefore < amountToRemove);
        }else{
            System.out.println("got here");
            return amountBefore == amountToRemove;
        }
    }

    public Boolean buyStock(String tickerSymbol, int numOfShares){
        Boolean isSuccessful;
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            stock.buyShares(numOfShares);
            setChanged();
            notifyObservers(ViewUpdateType.NUMBEROFSHARES);
            isSuccessful = true;
        }else{
            isSuccessful =  createStock(ticker, numOfShares);
        }

        return isSuccessful;
    }

    private Boolean createStock(String tickerSymbol, int numOfShares) {
        assert tickerSymbol != null;
        if(Prices.addTicker(tickerSymbol)){
            Stock newStock = new Stock(tickerSymbol, tickerSymbol + "Name", numOfShares);
            stockMap.put(tickerSymbol,newStock);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Portfolio){
            Portfolio other = (Portfolio) object;
            if(!other.getPortfolioName().equals(folioName)){
                return false;
            }
            Set<String> tickers = other.getStockTickers();
            if(tickers.size() != stockMap.keySet().size()){
                return false;
            }
            for(String name : tickers){
                IStock stock = other.getStockByTicker(name);
                if(!stockMap.containsKey(name)){
                    return false;
                }
                if(!stockMap.get(name).equals(stock)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        int runningHash = 0;
        runningHash += folioName.hashCode();
        for(String name : stockMap.keySet()){
            runningHash += stockMap.get(name).hashCode();
        }
        return runningHash;
    }
}