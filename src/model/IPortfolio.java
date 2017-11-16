package model;

import java.util.Set;

public interface IPortfolio {

    String getPortfolioName();

    Set<String> getStockTickers();

    IStock getStockByTicker(String ticker);

    void createStock(String tickerSymbol, int numOfShares);

    boolean removeStock(String tickerSymbol);

    Double getTotalValue();

}
