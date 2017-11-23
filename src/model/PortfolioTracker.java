package model;

import model.IPortfolioTracker;
import view.StockTable;

import javax.sound.sampled.Port;
import java.io.*;
import java.util.*;

public class PortfolioTracker extends Observable implements IPortfolioTracker {

    private Map<String, Portfolio> portfolioList;
    private String fileName;
    private Prices prices;

    public PortfolioTracker() {
        portfolioList = new HashMap<>();
        prices = new Prices();
        this.fileName = fileName;
      //   populate();
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
    public Boolean deletePortfolioByName(String name) {
        if(portfolioList.containsKey(name)){
            portfolioList.remove(name);
            setChanged();
            notifyObservers(ViewUpdateType.DELETION);
            return true;
        }
        return false;
    }

    @Override
    public Boolean createPortfolio(String name) {
        if(!portfolioList.containsKey(name)){
            createAndAdd(name);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            return true;
        }
        return false;
    }

    @Override
    public Boolean savePortfolios(String fileName) {
            try {
                FileOutputStream outPut = new FileOutputStream(fileName);
                ObjectOutputStream out = new ObjectOutputStream(outPut);
                for (Portfolio portfolio : portfolioList.values()) {
                    out.writeObject(portfolio);
                }
                out.close();
                outPut.close();
                System.out.println("Data is saved in: " + fileName);
                return true;
            } catch (IOException i) {
                i.printStackTrace();
            }
        return false;
    }


    @Override
    public Boolean loadPortfolioFromFile(String fileName) {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                Object inputObject = null;
                while (true) {
                    System.out.println("Reading: ");
                    inputObject = in.readObject();
                    portfolioList.put(((Portfolio) inputObject).getPortfolioName(), (Portfolio) inputObject);
                }
            } catch (ClassNotFoundException | EOFException e) {
                System.out.println("Exception: ");
                in.close();
                fileIn.close();
                for (Portfolio portfolio : portfolioList.values()) {
                    System.out.println("folio: " + portfolio.getPortfolioName());
                    for(String name : portfolio.getStockTickers()){
                        Prices.addTicker(name);
                    }
                }
                setChanged();
                notifyObservers(ViewUpdateType.CREATION);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addObserverToFolio(String name, Observer table) {
        if(portfolioList.containsKey(name)){
            portfolioList.get(name).addObserver(table);
            return true;
        }
        return false;
    }
}
