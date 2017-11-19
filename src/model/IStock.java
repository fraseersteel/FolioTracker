package model;

public interface IStock {

    // getters
    String getTicketSymbol();

    String getStockName();

    int getNumShares();

    double getPricePerShare();

    double getValueOfHolding();


    //setters
    void setStockName(String stockName);

    void setNumShares(int numShares);

    void setPricePerShare(double pricePerShare);

    void setValueOfHolding(double valueOfHolding);

    void deleteStock(Stock stock);
}
