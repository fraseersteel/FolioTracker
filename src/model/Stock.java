package model;

public class Stock implements IStock {

    private String ticketSymbol;
    private String stockName;
    private int numShares;
    private double pricePerShare;
    private double valueOfHolding;



    public Stock(String ticketSymbol,String stockName, int numShares, double pricePerShare, double valueOfHolding){
        this.ticketSymbol = ticketSymbol;
        this.stockName = stockName;
        this.numShares = numShares;
        this.pricePerShare = pricePerShare;
        this.valueOfHolding = valueOfHolding;
    }

    public String getTicketSymbol() {
        return ticketSymbol;
    }

    public String getStockName() {
        return this.stockName;
    }

    public int getNumShares() {
        return numShares;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public double getValueOfHolding() {
        return valueOfHolding;
    }

    @Override
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setNumShares(int numShares){
        this.numShares = numShares;
    }

    public void setPricePerShare(int pricePerShare){
        this.pricePerShare = pricePerShare;
    }

    public void setValueOfHolding(int valueOfHolding){
        this.valueOfHolding = valueOfHolding;
    }

    public void deleteStock(Stock stock){
        stock = null;
    }

}
