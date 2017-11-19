package model;

import model.IPortfolioTracker;
import view.StockTable;

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
//        populate();
        Thread thread = new Thread(() -> {
            while(true) {
                System.out.println("Refreshing");
                prices.refresh();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void populate(){
        createAndAdd("1test");
        createAndAdd("2test");
    }

    private void createAndAdd(String name){
        portfolioList.put(name, new Portfolio(name));
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

    @Override
    public boolean deletePortfolioByName(String name) {
        if(portfolioList.containsKey(name)){
            portfolioList.remove(name);
            setChanged();
            notifyObservers(ViewUpdateType.DELETION);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPortfolio(String name) {
        if(!portfolioList.containsKey(name)){
            createAndAdd(name);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            return true;
        }
        return false;
    }

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

    @Override
    public void addObserverToFolio(String name, Observer table) {
        portfolioList.get(name).addObserver(table);
    }
}
