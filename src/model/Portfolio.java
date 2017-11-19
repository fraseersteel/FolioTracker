package model;

import java.util.*;

public class Portfolio extends Observable implements IPortfolio {

    private Map<String, Stock> stockMap;
    private String folioName;

    public Portfolio(String folioName){
        this.folioName = folioName;
        stockMap = new HashMap<>();
        populate();
    }

    private void populate(){

        createStock("a",100);

        createStock("b", 400);

        if(folioName.startsWith("1test")){
            createStock("C",50);
        }else{
            createStock("d",20);
        }

//        Stock stock3 = new Stock("C","stockname3",1000,5.70,5700);
//        stockMap.put(stock3.getTicketSymbol(),stock3);
//
//        Stock stock4 = new Stock("D","stockname4",24000,0.01,24);
//        stockMap.put(stock4.getTicketSymbol(),stock4);
//
//        Stock stock5 = new Stock("E","stockname5",300,0.2,60);
//        stockMap.put(stock5.getTicketSymbol(),stock5);


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
    public Stock getStockByTicker(String name) {
        return stockMap.get(name);
    }

    public boolean sellStock(String tickerSymbol, int numOfShares){
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            if(numOfShares < stock.getNumShares()){
                stock.sellShares(numOfShares);
                notifyWith(ViewUpdateType.NUMBEROFSHARES);
            }else if(numOfShares == stock.getNumShares()){
                stockMap.remove(ticker);
                notifyWith(ViewUpdateType.DELETION);
            }else{
               System.out.println("Don't have that many shares..");
            }
            return true;
        }
        return false;
    }

    public boolean buyStock(String tickerSymbol, int numOfShares){
        String ticker = tickerSymbol.toUpperCase();
        Stock stock = stockMap.get(ticker);
        if(stock != null){
            stock.buyShares(numOfShares);
            notifyWith(ViewUpdateType.NUMBEROFSHARES);
            return true;
        }
        return createStock(ticker, numOfShares);
    }

    private boolean createStock(String tickerSymbol, int numOfShares) {
        String ticker = tickerSymbol.toUpperCase();
        if(Prices.addTicker(ticker)){
            Stock newStock = new Stock(ticker, ticker + "Name", numOfShares);
            stockMap.put(ticker,newStock);
            notifyWith(ViewUpdateType.CREATION);
            return true;
        }
        return false;
    }

    private void notifyWith(ViewUpdateType type){
        setChanged();
        notifyObservers(type);
    }

}