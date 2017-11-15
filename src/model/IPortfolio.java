package model;

public interface IPortfolio {

    String getPortfolioName();

    void setPortfolioName(String name);

    boolean createStock(String tickerSymbol, int numOfShares);

    boolean buyStock(String ticketSymbol, int numOfShares);

    boolean removeStock(String tickerSymbol);

}
