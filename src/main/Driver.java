package main;

import model.IPortfolio;
import model.Portfolio;
import view.*;
import model.IPortfolioTracker;

import java.util.ArrayList;

public class Driver {

    private FolioFrame view;
    private IPortfolioTracker model;
//    private Controller controller;

    public Driver() {
        this.view = new FolioFrame();

        ArrayList<IPortfolio> folio = new ArrayList<>();
        folio.add(new Portfolio("A"));
        folio.add(new Portfolio("b"));
        folio.add(new Portfolio("c"));
        view.setTabbedPane(folio);

//        try {
//            Thread.currentThread().sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        folio.add(new Portfolio("d"));
//        view.setTabbedPane(folio);





//        this.model = model;
//        this.controller = controller;
    }

    public static void main(String[] args) {

        Driver control = new Driver();

    }
}
