package model;

import model.IPortfolioTracker;
import view.StockTable;


import java.io.*;
import java.util.*;

public class PortfolioTracker extends Observable implements IPortfolioTracker {

    private Map<String, Portfolio> portfolioList;
    private Prices prices;

    public PortfolioTracker() {
        portfolioList = new HashMap<>();
        prices = new Prices();
    }

    public void startRefresher(){
        Thread thread = new Thread(() -> {
            while(true) {
                prices.refresh();
                try {
                    Thread.sleep(7000);
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
            assert !portfolioList.containsKey(name): "Looking for:" + name + " in " + portfolioList.keySet();
            System.out.println("profile found and now gone " + name);
            return true;
        }
        assert !portfolioList.containsKey(name): "Looking for:" + name + " in " + portfolioList.keySet();
        System.out.println("profile not found " + name);
        return false;
    }

    @Override
    public Boolean createPortfolio(String name) {
        if(!portfolioList.containsKey(name)){
            createAndAdd(name);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            assert portfolioList.containsKey(name): "Looking for:" + name + " in " + portfolioList.keySet();
            return true;
        }
        assert portfolioList.containsKey(name): "Looking for:" + name + " in " + portfolioList.keySet();
        return false;
    }

    @Override
    public Boolean savePortfolios(File file) {
            try {
                System.setProperty("user.dir",file.getAbsolutePath());

                FileOutputStream outPut = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(outPut);
                out.writeObject(prices.getCurrentPriceList());
                for (Portfolio portfolio : portfolioList.values()) {
                    out.writeObject(portfolio);
                }
                out.close();
                outPut.close();
                System.out.println("Data is saved in: " + file.getName());
                return true;
            } catch (IOException i) {
                i.printStackTrace();
            }
        return false;
    }


    @Override
    public Boolean loadPortfolioFromFile(File file) {
        System.setProperty("user.dir", file.getAbsolutePath());
        int portfoliosBefore = portfolioList.values().size();
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                Object inputObject = null;
                while (true) {
                    System.out.println("Reading: ");
                    inputObject = in.readObject();
                    if(inputObject instanceof Map){
                        System.out.println("Found Prices");
                        this.prices = new Prices((Map<String, Double>) inputObject);
                        System.out.println("Put all: " + inputObject);
                    }else if(inputObject instanceof Portfolio){
                        portfolioList.put(((Portfolio) inputObject).getPortfolioName(), (Portfolio) inputObject);
                    }else{
                        return false;
                    }
                }
            } catch (EOFException e) {
                System.out.println("Exception: ");
                in.close();
                fileIn.close();
                if(portfoliosBefore == portfolioList.values().size()){
                    return false;
                }
                for (Portfolio portfolio : portfolioList.values()) {
                    System.out.println("loading folio: " + portfolio.getPortfolioName());
                    for(String name : portfolio.getStockTickers()){
                        Prices.addTicker(name);
                    }
                }
                setChanged();
                notifyObservers(ViewUpdateType.CREATION);
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Boolean addObserverToFolio(String name, Observer observer) {
        if(portfolioList.containsKey(name)){
            portfolioList.get(name).addObserver(observer);
            return true;
        }
        return false;
    }
}
