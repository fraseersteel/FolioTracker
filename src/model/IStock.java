package model;

public interface IStock {

    String getTickerSymbol();

    String getStockName();

    int getNumShares();

    Double getPricePerShare();

    Double getValueOfHolding();

    Double getInitalPricePerShare();

    Double getProfitOfHolding();

    void setStockName(String stockName);
}
