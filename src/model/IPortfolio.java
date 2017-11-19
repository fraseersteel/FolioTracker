package model;

import java.util.Observer;
import java.util.Set;

public interface IPortfolio {

    String getPortfolioName();

    Set<String> getStockTickers();

    IStock getStockByTicker(String ticker);

    boolean buyStock(String tickerSymbol, int numOfShares);

    boolean sellStock(String tickerSymbol, int numOfShares);
}
