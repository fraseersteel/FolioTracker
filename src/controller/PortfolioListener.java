package controller;

import model.IPortfolio;
import model.IPortfolioTracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortfolioListener implements ActionListener {

    private IPortfolioTracker portfolio;

    public PortfolioListener(IPortfolioTracker portfolio) {
        this.portfolio = portfolio;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
//        String selectedTickerName = stockTable.getSelectedTicker();
//        int numOfShares           = stockTable.getSelectedNum();
        switch(action){
            case "buy":
                //            folioModel.buyStock(selectedTickerName, numOfShares);
                break;
            case "sell":
                //            folioModel.sellStock(selectedTickerName, numOfShares);
                break;
            case "change name":
                break;
        }
    }
}
