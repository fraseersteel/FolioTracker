package model;

import java.util.List;
import java.util.Observer;
import java.util.Set;

public interface IPortfolioTracker {

    Set<String> getPortfolioNames();

    IPortfolio getPortfolioByName(String name);

    void addObserverToPrices(Observer observer);

    void savePortfolios();

    void loadPortfolioFromFile();

}
