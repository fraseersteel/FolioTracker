package model;

import model.QuoteServer.StrathQuoteServer;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class Stock implements IStock,Serializable {

    private static final long serialVersionUID = 42L;

    private String ticketSymbol;
    private String stockName;
    private int numShares;
    private double initialPrice;

    public Stock(String ticketSymbol,String stockName, int numShares){
        this.ticketSymbol = ticketSymbol;
        this.stockName = stockName;
        this.numShares = numShares;
        initialPrice = getPricePerShare();
    }

    public Stock(Stock stock){
        this.ticketSymbol = stock.getTickerSymbol();
        this.stockName = stock.getStockName();
        this.numShares = stock.getNumShares();
        initialPrice = stock.getInitalPricePerShare();
    }

    public String getTickerSymbol() {
        return new String(ticketSymbol);
    }

    public String getStockName() {
        return new String(stockName);
    }

    public int getNumShares() {
        return numShares;
    }

    public Double getPricePerShare() {
        return Prices.getPriceOfTicker(ticketSymbol);
    }

    public Double getInitalPricePerShare(){
        return new Double(initialPrice);
    }

    public Double getValueOfHolding() {
        return Prices.getPriceOfTicker(ticketSymbol)*numShares;
    }

    public Double getProfitOfHolding(){
        return (getValueOfHolding()-(numShares*initialPrice));
    }

    public void setStockName(String stockName) { this.stockName = stockName; }

    public void buyShares(int numShares){
        this.numShares += numShares;
    }

    public void sellShares(int numShares){
        this.numShares -= numShares;
    }
}
