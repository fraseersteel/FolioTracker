package model;

public interface IStock {

    // getters
    String getTickerSymbol();

    String getStockName();

    int getNumShares();

    Double getPricePerShare();

    Double getValueOfHolding();


    //setters
    void setStockName(String stockName);
}
