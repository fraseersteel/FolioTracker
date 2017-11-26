package controller;

import model.IPortfolio;
import view.IFolioFrame;
import view.IStockTable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class StockListener implements ActionListener, TableModelListener {

    private static final String ADD = "Add Stock";
    private static final String BUY = "Buy Shares";
    private static final String SELL = "Sell Shares";

    private IPortfolio folioModel;
    private IStockTable stockTable;
    private IFolioFrame folioFrame;

    private boolean passed = false;

    public StockListener(IFolioFrame folioFrame, IPortfolio folioModel, IStockTable stockTable) {
        this.folioFrame = folioFrame;
        this.stockTable = stockTable;
        this.folioModel = folioModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        String ticker = stockTable.getSelectedTicker();

        if(ticker == null && !action.equals(ADD)){
            folioFrame.displayError("Row not selected\nPlease select the row with the desired stock.");
            return;
        }
        while(!passed) {
            String result = stockTable.displayOption(action, action.equals(ADD));
            if(!result.equals("-2")){
                try{
                    int i = Integer.parseInt(result);
                    if (i > 1000000000 || i < 1) {
                        folioFrame.displayError("Please enter a number within the range 1 to 1000000000.");
                    }else {
                        startBackGroundWork(action, ticker, i);
                    }
                }catch (NumberFormatException err){
                    folioFrame.displayError("Please enter a number for the number of shares.");
                }
            }else{
                passed = true;
            }
        }
        passed = false;
    }

    private void startBackGroundWork(String action, String ticker, int amount){


        SwingWorker<Boolean,Void> worker = new SwingWorker<Boolean, Void>()
        {
            @Override
            protected Boolean doInBackground()
            {
                if(action.equals(ADD)){
                    String t = stockTable.getTickerInput();
                    if(t == null || t.equals("")){
                        folioFrame.displayError("Please enter a ticker name.");
                    }else{
                        t= t.replaceAll("\\s+","");
                    }
                    return folioModel.buyStock(t, amount);
                }else if(action.equals(BUY)){
                    return folioModel.buyStock(ticker, amount);
                }else{
                    assert action.equals(SELL) : action;
                    return folioModel.sellStock(ticker, amount);
                }
            }

            @Override
            protected void done()
            {
                try {
                    Boolean pass = get();
                    if(!pass && action.equals(SELL)){
                        folioFrame.displayError("Not enough shares \nyou have tried to sell more shares than you own!");
                        passed=false;
                    }else if(pass == null){
                        folioFrame.displayError("Stock Not Found");
                        passed=false;
                    } else if(pass){
                        passed = true;
                    }else{
                        String t = stockTable.getTickerInput();
                        if(t == null || t.equals("")){
                            folioFrame.displayError("Please enter a ticker name.");
                        }else{
                            t= t.replaceAll("\\s+","");
                        }
                        folioFrame.displayError("No such Ticker Found. [" + t + "]. The ticker doesn't exist.");
                        passed=false;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }finally {
                    folioFrame.hideLoadingScreen();
                }
            }
        };
        worker.execute();
        folioFrame.showLoadingScreen("Searching for Ticker...");
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            if (e.getColumn() == 1) {
                String ticker = stockTable.getSelectedTicker();
                String newName = (String) stockTable.getValueAt(e.getFirstRow(), e.getColumn());
                folioModel.setNameOfStock(ticker, newName);
            }
        }
    }
}
