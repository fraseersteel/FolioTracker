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

        view.insertProfile("A", new StockTable());
        view.insertProfile("B", new StockTable());
        view.insertProfile("C", new StockTable());

        view.addStockToFolio("A", "Tk1", "stock1", 50, 1.5);
        view.addStockToFolio("B", "Tk2", "stock2", 40, 6.1);
        view.addStockToFolio("C", "Tk3", "stock3", 30, 0.6);
        view.addStockToFolio("C", "Tk4", "stock4", 20, 0.2);


        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        view.insertProfile("D", new StockTable());

        StockTable table = view.getFolioStockTable("A");
        table.insertValues("z1", "zzzzz", 10, 40.0);


        table.buyStocks("aasd", "alp stocks", 1);
        table.confirmBuy("a", 1);


        table.sellStocks();


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
