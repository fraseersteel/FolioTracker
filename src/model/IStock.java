package model;

public interface IStock {

    // getters
    String getTicketSymbol();

    String getStockName();

    int getNumShares();

    Double getPricePerShare();

    Double getValueOfHolding();


    //setters
    void setStockName(String stockName);

    void setNumShares(int numShares);

//    void setPricePerShare(int pricePerShare);
//
//    void setValueOfHolding(int valueOfHolding);

}
