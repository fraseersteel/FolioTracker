package controller;

import model.IPortfolio;
import model.IStock;
import view.IStockTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockListener implements ActionListener {

    private IPortfolio folioModel;
    private IStockTable stockTable;

    public StockListener(IPortfolio folioModel, IStockTable stockTable) {
        this.stockTable = stockTable;
        this.folioModel = folioModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        System.out.println("ACTION: " + action);
//        String selectedTickerName = stockTable.getSelectedTicker();
//        int numOfShares           = stockTable.getSelectedNum();
        switch(action){
            case "Buy Stocks":
                
                break;
            case "Sell Stocks":
                //            folioModel.sellStock(selectedTickerName, numOfShares);
                break;
            case "change name":
                break;
        }
    }
}
