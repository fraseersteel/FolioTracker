package main;

import model.PortfolioTracker;
import model.Prices;
import view.*;
import model.IPortfolioTracker;

public class Driver {

    private PortfolioTracker model;
    private FolioFrame folioFrame;

    public Driver() {
        model = new PortfolioTracker();
        folioFrame = new FolioFrame(model);
        model.addObserver(folioFrame);
    }

    public static void main(String[] args) {
        Driver control = new Driver();
    }
}
