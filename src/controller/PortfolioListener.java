package controller;

import model.IPortfolio;
import model.IPortfolioTracker;
import view.IFolioFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PortfolioListener implements ActionListener {

    private IPortfolioTracker model;
    private IFolioFrame view;

    private JTextField xField = new JTextField(15);

    public PortfolioListener(IPortfolioTracker model,IFolioFrame view) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        switch(action){
            case "New Folio":
                newFolio();
                break;
            case "Open Folio From File":
                break;
            case "Save Folios":
                model.savePortfolios();
                break;
            case "Delete Folio":
                delete();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    private void delete() {
        String folioName = view.getSelectedFolio();
        if(folioName == null){
            displayError("No Folio To Delete.");
            return;
        }
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete folio: " + folioName,
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
                String text = xField.getText();
                if(text == null || text.equals("")){
                    displayError("Creation failed, please enter a name");
                }else if(!model.createPortfolio(text)){
                     displayError("Creation failed, is the name already in use?");
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
        myPanel.add(xField);

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
