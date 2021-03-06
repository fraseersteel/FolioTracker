package model;

import view.StockTable;

import java.io.File;
import java.util.List;
import java.util.Observer;
import java.util.Set;

public interface IPortfolioTracker {

    Set<String> getPortfolioNames();

    IPortfolio getPortfolioByName(String name);

    Boolean deletePortfolioByName(String name);

    Boolean createPortfolio(String name);

    Boolean savePortfolios(File file);

    Boolean loadPortfolioFromFile(File file);

    Boolean addObserverToFolio(String name, Observer observer);

    void addObserverToPrices(Observer observer);
}
