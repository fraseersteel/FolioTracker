package main;

import model.PortfolioTracker;
import model.Prices;
import view.*;
import model.IPortfolioTracker;

import javax.swing.*;

public class Driver {

    public static void main(String[] args) {
        PortfolioTracker model = new PortfolioTracker();
        SwingUtilities.invokeLater(() -> {
            FolioFrame folioFrame = new FolioFrame(model);
            model.addObserver(folioFrame);
        });
    }
}
