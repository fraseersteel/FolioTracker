package model;

public interface IStock {

    // getters
    String getTickerSymbol();

    String getStockName();

    int getNumShares();

    Double getPricePerShare();

    Double getValueOfHolding();

    Double getInitalPricePerShare();

    Double getProfitOfHolding();


    //setters
    void setStockName(String stockName);
}
