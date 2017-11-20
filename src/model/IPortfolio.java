package model;

import java.util.Observer;
import java.util.Set;

public interface IPortfolio {

    String getPortfolioName();

    Set<String> getStockTickers();

    IStock getStockByTicker(String ticker);

    Boolean buyStock(String tickerSymbol, int numOfShares);

    Boolean sellStock(String tickerSymbol, int numOfShares);
}
