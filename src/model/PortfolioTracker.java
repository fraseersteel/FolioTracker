package model;

import model.IPortfolioTracker;

import javax.sound.sampled.Port;
import java.io.*;
import java.util.*;

public class PortfolioTracker extends Observable implements IPortfolioTracker {

    private Map<String, Portfolio> portfolioList;
    private List<Stock> stockList;
    private String fileName;
    private Prices prices;

    public PortfolioTracker() {
        portfolioList = new HashMap<>();
        this.fileName = "folioTracker.config";
        prices = new Prices();
        populate();
        Thread thread = new Thread(() -> {
            while(true) {
                System.out.println("Refreshing");
                prices.refresh();
            }
        });
        thread.start();
    }

    private void populate(){
        Portfolio port1 = new Portfolio("1test");
        portfolioList.put(port1.getPortfolioName(), port1);
        Portfolio port2 = new Portfolio("2test");
        portfolioList.put(port2.getPortfolioName(), port2);
    }

    @Override
    public Set<String> getPortfolioNames() {
        return portfolioList.keySet();
    }

    @Override
    public IPortfolio getPortfolioByName(String name) {
        return portfolioList.get(name);
    }

    @Override
    public void addObserverToPrices(Observer observer) {
        prices.addObserver(observer);
    }
//
//    public List<Portfolio> getPortFoilioList() {
//        return portfolioList.values();
//    }

    @Override
    public void savePortfolios() {
            try {
                FileOutputStream outPut = new FileOutputStream(fileName);
                ObjectOutputStream out = new ObjectOutputStream(outPut);
                for (Portfolio portfolio : portfolioList.values()) {
                    out.writeObject(portfolio);
                }
                for (Stock stock: stockList) {
                    out.writeObject(stock);
                }
                out.close();
                outPut.close();
                System.out.println("Data is saved in: " + fileName);
            } catch (IOException i) {
                i.printStackTrace();
            }

    }


    @Override
    public void loadPortfolioFromFile() {
        stockList.clear();
        portfolioList.clear();
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                Object inputObject = null;
                while (true) {
                    inputObject = in.readObject();
                    if (inputObject instanceof Portfolio)
                        portfolioList.put(((Portfolio) inputObject).getPortfolioName(), (Portfolio) inputObject);
                    else if (inputObject instanceof Stock)
                        stockList.add((Stock) inputObject);
                }
            } catch (ClassNotFoundException | EOFException e) {
                in.close();
                fileIn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
