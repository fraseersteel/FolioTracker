package controller;

import model.IPortfolio;
import model.IPortfolioTracker;

import javax.swing.*;
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
            case "Open":
                String newName;
                String amount;
                int amountInt;
                newName = JOptionPane.showInputDialog(null, "Enter Name of Stock you would like to open","Open Stock", 1);
                amount = JOptionPane.showInputDialog(null, "Enter number of Stocks you would like to buy","Open Stock", 1);
                try {
                    amountInt = Integer.parseInt(amount);
                    System.out.println(newName);
                    System.out.println(amountInt);
                }catch(NumberFormatException e1){
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
                // open new stock
                break;
            case "Save":
                portfolio.savePortfolios();
                break;
            case "Sell All":
                int n = JOptionPane.showConfirmDialog(null,"Are you sure you would like to sell all?","Sell all",JOptionPane.YES_NO_OPTION);
                System.out.println(n);
                if(n==0){
                //sell all stocks
                }else{
                // do nothing
                }
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }
}
