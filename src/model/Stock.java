package model;

import java.io.Serializable;

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

    @Override
    public boolean equals(Object object){
        if(object instanceof Stock){
            Stock other = (Stock) object;
            if(!ticketSymbol.equals(other.getTickerSymbol())){
                return false;
            }
            if(!stockName.equals(other.getStockName())){
                return false;
            }
            if(numShares != other.getNumShares()){
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        int runningHash = 0;
        runningHash += (ticketSymbol.hashCode()*ticketSymbol.hashCode());
        runningHash += (stockName.hashCode()*stockName.hashCode());
        runningHash += (numShares*numShares);
        return runningHash;
    }
}
