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

    private static final String ADD = "Add Stock";
    private static final String BUY = "Buy Shares";
    private static final String SELL = "Sell Shares";

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
        String action = e.getActionCommand();
        String ticker = stockTable.getSelectedTicker();

        if (ticker == null && !action.equals(ADD)) {
            displayError("Row not selected\nPlease select the row with the desired stock.");
            return;
        }
        boolean inputAccepted = false;
        while (!inputAccepted) {

            int result = JOptionPane.showConfirmDialog(null, getPanel(action.equals(ADD)),
                    action, JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                inputAccepted = doAction(action, ticker);
            } else {
                inputAccepted = true;
            }
        }
    }

    private boolean doAction(String action, String ticker) {
        try {
            int i = Integer.parseInt(sharesAmountField.getText());
            if (i > 1000000000 || i < 1) {
                displayError("Please enter a number within the range 1 to 1000000000.");
            } else {
                Boolean pass;
                //TODO start loading animation
                JDialog dialog = new JDialog();
                dialog.setTitle("Waiting");

                JPanel pane = new JPanel();
                dialog.setContentPane(pane);

                pane.add(new JLabel("WAITING"));

                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                dialog.pack();
                dialog.setVisible(true);
                dialog.setAlwaysOnTop (true);
                System.out.println("successful");


                if (action.equals(ADD)) {
                    ticker = tickerField.getText().replaceAll("\\s+", "");
                    pass = folioModel.buyStock(ticker, i);
                } else if (action.equals(BUY)) {
                    pass = folioModel.buyStock(ticker, i);
                } else {
                    assert action.equals(SELL) : action;
                    pass = folioModel.sellStock(ticker, i);
                }


                dialog.dispose();

                //TODO stop loading animation
                if (pass == null) {
                    displayError("Not enough shares\n you have tried to sell more shares than you own!");
                } else if (pass) {
                    return true;
                } else {
                    displayError("No such Ticker Found. [" + ticker + "]. The ticker doesn't exist.");
                }
            }
        } catch (NumberFormatException err) {
            displayError("Please enter a number for the number of shares.");
        }
        return false;
    }

    private JPanel getPanel(boolean withTickerField) {
        JPanel myPanel = new JPanel();
        GridLayout gridLayout;

        if (withTickerField) {
            gridLayout = new GridLayout(2, 2);
            myPanel.add(new JLabel("Ticker:"));
            myPanel.add(tickerField);
        } else {
            gridLayout = new GridLayout(1, 2);
        }
        myPanel.setLayout(gridLayout);

        myPanel.add(new JLabel("Number of shares:"));
        myPanel.add(sharesAmountField);

        return myPanel;
    }

    private void displayError(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Try again",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            if (e.getColumn() == 1) {
                String ticker = stockTable.getSelectedTicker();
                IStock stock = folioModel.getStockByTicker(ticker);
                stock.setStockName((String) stockTable.getValueAt(e.getFirstRow(), e.getColumn()));
            }
        }
    }
}
