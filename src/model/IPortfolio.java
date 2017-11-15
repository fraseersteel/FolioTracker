package model;

public interface IPortfolio {

    String getPortfolioName();

    void setPortfolioName(String name);

    void createStock(String tickerSymbol, int numOfShares);

    boolean removeStock(String tickerSymbol);

    Double getTotalValue();

}
