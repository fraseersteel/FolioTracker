package model;

import java.util.List;

public interface IPortfolioTracker {

    List<IPortfolio> getPortfolios();

    IPortfolio getPortfolioByName(String name);

    void savePortfolios();

    void loadPortfolioByName(String name);

}