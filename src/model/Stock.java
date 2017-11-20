package model;

import model.QuoteServer.StrathQuoteServer;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class Stock extends Observable implements IStock,Serializable {

    private static final long serialVersionUID = 42L;

    private String ticketSymbol;
    private String stockName;
    private int numShares;



    public Stock(String ticketSymbol,String stockName, int numShares){
        this.ticketSymbol = ticketSymbol;
        this.stockName = stockName;
        this.numShares = numShares;
    }

    public Stock(Stock stock){
        this.ticketSymbol = stock.getTickerSymbol();
        this.stockName = stock.getStockName();
        this.numShares = stock.getNumShares();
    }

    public String getTickerSymbol() {
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

    public void buyShares(int numShares){
        this.numShares += numShares;
    }

    public void sellShares(int numShares){
        this.numShares -= numShares;
    }
}
