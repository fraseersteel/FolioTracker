package model;

import model.IPortfolioTracker;

import java.util.List;

public class PortfolioTracker implements IPortfolioTracker {
    @Override
    public List<IPortfolio> getPortfolios() {
        return null;
    }

    @Override
    public IPortfolio getPortfolioByName(String name) {
        return null;
    }

    @Override
    public void savePortfolios() {

    }

    @Override
    public void loadPortfolioByName(String name) {

    }
}
