package model;

import java.util.Set;

public interface IPortfolio {

    String getPortfolioName();

    Set<String> getStockTickers();

    IStock getStockByTicker(String ticker);

    void buyStock(String tickerSymbol, int numOfShares);

    void sellStock(String tickerSymbol, int numOfShares);
}
