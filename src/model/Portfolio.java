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

        createStock("A",100);

        createStock("B", 400);

        if(folioName.startsWith("1test")){
            createStock("C",50);
        }else{
            createStock("D",20);
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

    public void sellStock(String tickerSymbol, int numOfShares){

    }

    private boolean removeStock(String tickerSymbol) {
        return stockMap.remove(tickerSymbol) != null;
    }

    public void buyStock(String tickerSymbol, int numOfShares){
        if(stockMap.containsKey(tickerSymbol)){
            stockMap.get(tickerSymbol);
        }else{
            createStock(tickerSymbol, numOfShares);
        }
    }

    private void createStock(String tickerSymbol, int numOfShares) {
        Stock newStock = new Stock(tickerSymbol, tickerSymbol + "Name", numOfShares);
        stockMap.put(tickerSymbol,newStock);
        Prices.addTicker(tickerSymbol);
    }

}