package model;

import java.util.Observer;
import java.util.Set;

public interface IPortfolio {

    String getPortfolioName();

    Set<String> getStockTickers();

    Boolean setNameOfStock(String ticker, String newName);

    IStock getStockByTicker(String name);

    Boolean buyStock(String ticker, int numOfShares);

    Boolean sellStock(String ticker, int numOfShares);
}
