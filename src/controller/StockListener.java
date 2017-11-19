package controller;

import model.IPortfolio;
import model.IStock;
import view.IStockTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StockListener implements ActionListener {

    private IPortfolio folioModel;
    private IStockTable stockTable;

    private JTextField xField = new JTextField(15);
    private JTextField yField = new JTextField(15);

    public StockListener(IPortfolio folioModel, IStockTable stockTable) {
        this.stockTable = stockTable;
        this.folioModel = folioModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        System.out.println("ACTION: " + action);
        String ticker = stockTable.getSelectedTicker();

        switch(action){
            case "Buy Stocks":
                buy(ticker);
                break;
            case "Sell Stocks":
                sell(ticker);
                break;
            case "add new stock":
                add();
                break;
            case "change name":
                break;
        }
    }
    //TODO remove duplication from next three methods
    private void sell(String ticker){
        if(ticker == null){
            displayError("Row not selected");
            return;
        }
        boolean inputAccepted = false;
        while(!inputAccepted) {

            int result = JOptionPane.showConfirmDialog(null, getPanel("SELL", false),
                    "Sell Stocks", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try{
                    int i = Integer.parseInt(yField.getText());
                    if(i > 1000000000 || i < 1){
                        displayError("Please enter a number within the range 1 to 1000000000.");
                    }else{
                        if(folioModel.sellStock(ticker, i)){
                            inputAccepted = true;
                        }else{
                            displayError("No such Ticker.");
                        }
                    }
                }catch (Exception err){
                    displayError("Please enter a number \n for amount of stock.");
                }

            } else {
                inputAccepted = true;
            }
        }
    }

    private void add(){
        boolean inputAccepted = false;
        while(!inputAccepted) {

            int result = JOptionPane.showConfirmDialog(null, getPanel("BUY", true),
                    "Buy Stocks", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try{
                    int i = Integer.parseInt(yField.getText());
                    if(i > 1000000000 || i < 1){
                        displayError("Please enter a number within the range 1 to 1000000000.");
                    }else{
                        if(folioModel.buyStock(xField.getText(), i)){
                            inputAccepted = true;
                        }else{
                            displayError("No such Ticker.");
                        }
                    }
                }catch (Exception err){
                    displayError("Please enter a number \n for amount of stock.");
                }

            } else {
                inputAccepted = true;
            }
        }
    }

    private void buy(String ticker){
        if(ticker == null){
            displayError("Row not selected");
            return;
        }
        boolean inputAccepted = false;
        while(!inputAccepted) {

            int result = JOptionPane.showConfirmDialog(null, getPanel("BUY", false),
                    "Buy Stocks", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try{
                    int i = Integer.parseInt(yField.getText());
                    if(i > 1000000000 || i < 1){
                        displayError("Please enter a number within the range 1 to 1000000000.");
                    }else{
                        if(folioModel.buyStock(ticker, i)){
                            inputAccepted = true;
                        }else{
                            displayError("No such Ticker.");
                        }
                    }
                }catch (Exception err){
                    displayError("Please enter a number \n for amount of stock.");
                }

            } else {
                inputAccepted = true;
            }
        }
    }

    private JPanel getPanel(String text, boolean withTickerField) {
        JPanel myPanel = new JPanel();
        GridLayout gridLayout;

        if(withTickerField){
            gridLayout = new GridLayout(2,2);
            myPanel.add(new JLabel("Ticker:"));
            myPanel.add(xField);
        }else{
            gridLayout = new GridLayout(1,2);
        }
        myPanel.setLayout(gridLayout);

        myPanel.add(new JLabel("AmountOfStock:"));
        myPanel.add(yField);

        return myPanel;
    }

    private void displayError(String msg){
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Try again",
                JOptionPane.ERROR_MESSAGE);
    }
}
