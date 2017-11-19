package controller;

import model.IPortfolio;
import model.IStock;
import view.IStockTable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockListener implements ActionListener, TableModelListener {

    private static final int ADD = 1;
    private static final int BUY = 2;
    private static final int SELL = 3;

    private static Lock lock = new ReentrantLock();

    private IPortfolio folioModel;
    private IStockTable stockTable;

    private JTextField tickerField = new JTextField(15);
    private JTextField sharesAmountField = new JTextField(15);

    public StockListener(IPortfolio folioModel, IStockTable stockTable) {
        this.stockTable = stockTable;
        this.folioModel = folioModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread thread = new Thread(() -> {
            String action = e.getActionCommand();
            System.out.println("ACTION: " + action);
            String ticker = stockTable.getSelectedTicker();

            switch(action){
                case "Buy Stocks":
                    prompt(ticker, "Buy Stocks", BUY);
                    break;
                case "Sell Stocks":
                    prompt(ticker, "Sell Stocks", SELL);
                    break;
                case "Add Stock":
                    prompt(ticker, "Add New Stock", ADD);
                    break;
            }
        });
        thread.start();
    }

    private void prompt(String ticker, String msg, int option){
        if(ticker == null && option != ADD){
            displayError("Row not selected");
            return;
        }
        boolean inputAccepted = false;
        while(!inputAccepted) {

            int result = JOptionPane.showConfirmDialog(null, getPanel(option ==ADD),
                    msg, JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try{
                    int i = Integer.parseInt(sharesAmountField.getText());
                    if(i > 1000000000 || i < 1){
                        displayError("Please enter a number within the range 1 to 1000000000.");
                    }else{
                        boolean pass;
                        if(option == ADD){
                            String tickerWithoutSpace = tickerField.getText().replaceAll("\\s+","");
                            lock.lock();
                            try{
                                pass = folioModel.buyStock(tickerWithoutSpace, i);
                            }finally {
                                lock.unlock();
                            }
                        }else if(option ==BUY){
                            pass = folioModel.buyStock(ticker, i);
                        }else{
                            assert option == 3 : option;
                            pass = folioModel.sellStock(ticker, i);
                        }
                        if(pass){
                            inputAccepted = true;
                        }else{
                            displayError("No such Ticker. [" + ticker + "]");
                        }
                    }
                }catch (NumberFormatException err){
                    displayError("Please enter a number for amount of stock.");
                }

            } else {
                inputAccepted = true;
            }
        }
    }

    private JPanel getPanel(boolean withTickerField) {
        JPanel myPanel = new JPanel();
        GridLayout gridLayout;

        if(withTickerField){
            gridLayout = new GridLayout(2,2);
            myPanel.add(new JLabel("Ticker:"));
            myPanel.add(tickerField);
        }else{
            gridLayout = new GridLayout(1,2);
        }
        myPanel.setLayout(gridLayout);

        myPanel.add(new JLabel("AmountOfStock:"));
        myPanel.add(sharesAmountField);

        return myPanel;
    }

    private void displayError(String msg){
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Try again",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            if(e.getColumn() == 1){
                String ticker = stockTable.getSelectedTicker();
                IStock stock = folioModel.getStockByTicker(ticker);
                stock.setStockName((String) stockTable.getValueAt(e.getFirstRow(), e.getColumn()));
            }
        }
    }
}
