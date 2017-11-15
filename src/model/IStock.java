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

    void setPricePerShare(int pricePerShare);

    void setValueOfHolding(int valueOfHolding);

}
