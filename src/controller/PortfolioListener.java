package controller;

import model.IPortfolio;
import model.IPortfolioTracker;
import view.IFolioFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PortfolioListener implements ActionListener {

    private IPortfolioTracker model;
    private IFolioFrame view;

    private JTextField folioNameField = new JTextField(15);

    public PortfolioListener(IPortfolioTracker model,IFolioFrame view) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch(action){
            case "New":
                newFolio();
                break;
            case "Load Folio From File":
                //TODO loading screen
                Boolean b = model.loadPortfolioFromFile();
                //TODO stop loading screen
                if(!b){
                    displayError("Error loading Folios\n Please Make sure the file exists and is not empty.");
                }
                break;
            case "Open":
                open();
                break;
            case "Hide":
                if(!view.hideSelectedFolio()){
                    displayError("Error Hiding Folio\n Please select a folio to hide.");
                }
                break;
            case "Save Folios":
                model.savePortfolios();
                break;
            case "Delete":
                delete();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    private void open(){
        int result = JOptionPane.showConfirmDialog(null, getPanel(),
                "Delete Selected Folio", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = folioNameField.getText();
            if(name == null || name.equals("")){
                displayError("Error opening Folio\n Please enter a name.");
            }else if(model.getPortfolioByName(name) == null){
                displayError("Error opening Folio\n Folio does not exist.");
            }else if(!view.showFolio(folioNameField.getText())){
                displayError("Error opening Folio\n Folio is already showing with that name.");
            }
        }
    }

    private void delete() {
        String folioName = view.getSelectedFolio();
        if(folioName == null){
            displayError("No Folio To Delete.");
            return;
        }
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete folio: [" + folioName + "]",
                "Delete Selected Folio", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            model.deletePortfolioByName(folioName);
        }
    }

    private void newFolio(){
        boolean inputAccepted = false;
        while(!inputAccepted) {

            int result = JOptionPane.showConfirmDialog(null, getPanel(),
                    "New Folio", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String text = folioNameField.getText();
                if(text == null || text.equals("")){
                    displayError("Creation failed, please enter a name.");
                }else if(!model.createPortfolio(text)){
                     displayError("Creation failed, name ["+text+ "] is already in use.");
                }else{
                    inputAccepted = true;
                }
            } else {
                inputAccepted = true;
            }
        }
    }

    private JPanel getPanel() {
        JPanel myPanel = new JPanel();
        GridLayout gridLayout;

        gridLayout = new GridLayout(1,1);
        myPanel.setLayout(gridLayout);
        myPanel.add(new JLabel("Folio Name:"));
        myPanel.add(folioNameField);

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
