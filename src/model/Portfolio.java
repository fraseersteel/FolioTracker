package model;

import java.util.*;

public class Portfolio extends Observable implements IPortfolio {

    private Map<String, Stock> stockMap;
    private String folioName;

    public Portfolio(String folioName) {
        this.folioName = folioName;
        stockMap = new HashMap<>();
        populate();
    }

    private void populate() {

        Stock stock1 = new Stock("1", "stockname1", 100, 2.30, 230);
        stockMap.put(stock1.getTicketSymbol(), stock1);//map with ticket symbol/name?

        Stock stock2 = new Stock("2", "stockname2", 400, 0.30, 120);
        stockMap.put(stock2.getTicketSymbol(), stock2);

        Stock stock3 = new Stock("3", "stockname3", 1000, 5.70, 5700);
        stockMap.put(stock3.getTicketSymbol(), stock3);

        Stock stock4 = new Stock("4", "stockname4", 24000, 0.01, 24);
        stockMap.put(stock4.getTicketSymbol(), stock4);

        Stock stock5 = new Stock("1", "stockname5", 300, 0.2, 60);
        stockMap.put(stock5.getTicketSymbol(), stock5);


    }//mock objects for portfolio


    @Override
    public String getPortfolioName() {
        return folioName;
    }

    @Override
    public void setPortfolioName(String name) {
        folioName = name;
    }

    @Override
    public boolean removeStock(String tickerSymbol) {
        try {
            //TODO handle invalid ticker
            stockMap.remove(tickerSymbol);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean createStock(String tickerSymbol, int numOfShares) {
        try {
            Stock newStock = new Stock(tickerSymbol, tickerSymbol + "Name", numOfShares, 1, numOfShares);//get priceper share from prices?
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean buyStock(String tickerSymbol, int numOfShares) {
        try {
            Stock newStock = new Stock(tickerSymbol, tickerSymbol + "Name", numOfShares, 1, numOfShares);//get priceper share from prices?
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void getStocks(){
        for(Stock stock:stockMap.values()){
            System.out.println(stock.getTicketSymbol());
        }
    }



}