package controller;

import model.IPortfolio;
import model.IPortfolioTracker;
import view.IFolioFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PortfolioListener implements ActionListener {

    private IPortfolioTracker model;
    private IFolioFrame view;

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
                load();
                break;
            case "Open":
                view.promptFolioToShow();
                break;
            case "Hide":
                if(!view.hideSelectedFolio()){
                    view.displayError("Error Hiding Folio\n Please select a folio to hide.");
                }
                break;
            case "Save Folios":
                save();
                break;
            case "Delete":
                delete();
                break;
        }
    }

    private void save() {
        if(model.getPortfolioNames().size() == 0){
            view.displayError("There are no folios to save. \n Please make sure that you have at least one folio open before trying to save.");
        }else{
            File fileSave = view.promptFileChooser(false);
            if(fileSave != null){
                Boolean b = model.savePortfolios(fileSave);
                if(!b){
                    view.displayError("Error Saving Folios\n");
                }
            }
        }
    }

    private void load() {
        boolean continueLoad = view.promptConfirmation("Warning","Loading from a file will delete all current folios, do you wish to continue? \n make sure to Save your current work.");
        if(continueLoad){
            File fileLoad = view.promptFileChooser(true);
            if(fileLoad != null){
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        List<String> names = new ArrayList<>();
                        for(String name : model.getPortfolioNames()){
                            System.out.println("deleting " + name);
                            names.add(name);
                        }
                        for(String name : names){
                            model.deletePortfolioByName(name);
                        }
                        return model.loadPortfolioFromFile(fileLoad);
                    }

                    @Override
                    protected void done() {
                        try {
                            if (!get()) {
                                view.displayError("Error loading Folios\n The file chosen is not a valid folioTracker file\n Make sure your chosen file has the extension: .foliot");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }finally {
                            view.hideLoadingScreen();
                        }
                    }
                };
                worker.execute();
                view.showLoadingScreen("Loading Selected File...");
            }
        }
    }

    private void delete() {
        String folioName = view.getSelectedFolio();
        if(folioName == null){
            view.displayError("No Folio To Delete.");
            return;
        }
        boolean result = view.promptConfirmation("Delete Selected Folio", "Are you sure you want to delete folio: [" + folioName + "]");

        if (result) {
            model.deletePortfolioByName(folioName);
        }
    }

    private void newFolio(){
        boolean inputAccepted = false;
        while(!inputAccepted) {
            String name = view.promptForString("Enter the name of your new folio:");

            if (name != null) {
                if(name.isEmpty()){
                    view.displayError("Name Cannot be empty.");
                }else{
                    if(!model.createPortfolio(name)){
                        view.displayError("Creation failed, name ["+name+ "] is already in use.");
                    }else{
                        inputAccepted = true;
                    }
                }
            } else {
                inputAccepted = true;
            }
        }
    }
}
