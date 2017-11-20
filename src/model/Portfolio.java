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
//        populate();
    }

    private void populate(){

        createStock("a",100);

        createStock("b", 400);

        if(folioName.startsWith("1test")){
            createStock("C",50);
        }else{
            createStock("d",20);
        }
    }

    @Override
    public String getPortfolioName() {
        return folioName;
    }

    @Override
    public Set<String> getStockTickers() {
        return stockMap.keySet();
    }

    @Override
    public IStock getStockByTicker(String name) {
        return new Stock(stockMap.get(name));
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
                System.out.println("Don't have that many shares..");
                return false;
            }
            return true;
        }
        return null;
    }

    public Boolean buyStock(String tickerSymbol, int numOfShares){
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            System.out.println("buying shares:" + ticker + " with :" + numOfShares);
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
            System.out.println("created ticker:" + ticker + " with :" + numOfShares);
            Stock newStock = new Stock(ticker, ticker + "Name", numOfShares);
            stockMap.put(ticker,newStock);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            return true;
        }
        return null;
    }
}