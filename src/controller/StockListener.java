package controller;

import model.IPortfolio;
import model.IStock;
import view.IStockTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockListener implements ActionListener {

    private IPortfolio folioModel;
    private IStockTable stockTable;

    public StockListener(IPortfolio folioModel) {
        this.folioModel = folioModel;
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
