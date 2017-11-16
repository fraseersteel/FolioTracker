package model;

import model.QuoteServer.StrathQuoteServer;

import java.util.Observable;
import java.util.Observer;

public class Stock extends Observable implements IStock {

    private String ticketSymbol;
    private String stockName;
    private int numShares;
//    private double pricePerShare;
//    private double valueOfHolding;



    public Stock(String ticketSymbol,String stockName, int numShares){
        this.ticketSymbol = ticketSymbol;
        this.stockName = stockName;
        this.numShares = numShares;
//        this.pricePerShare = pricePerShare;
//        this.valueOfHolding = valueOfHolding;
    }

    public String getTicketSymbol() {
        return ticketSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public int getNumShares() {
        return numShares;
    }

    public Double getPricePerShare() {
        return Prices.getPriceOfTicker(ticketSymbol);
    }

    public Double getValueOfHolding() {
        return Prices.getPriceOfTicker(ticketSymbol)*numShares;
    }

    @Override
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setNumShares(int numShares){
        this.numShares = numShares;
    }

//    public void setPricePerShare(int pricePerShare){
//        this.pricePerShare = pricePerShare;
//    }

//    public void setValueOfHolding(int valueOfHolding){
//        this.valueOfHolding = valueOfHolding;
//    }

}
