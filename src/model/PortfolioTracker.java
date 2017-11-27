package model;

import QuoteServer.QuoteServer;
import QuoteServer.StrathQuoteServer;
import model.IPortfolioTracker;
import view.StockTable;


import java.io.*;
import java.util.*;

public class PortfolioTracker extends Observable implements IPortfolioTracker {

    private Map<String, Portfolio> portfolioList;
    Timer timer;
    private Prices prices;

    public PortfolioTracker(QuoteServer quoteServer) {
        timer = new Timer();
        portfolioList = new HashMap<>();
        prices = new Prices(quoteServer);
    }

    public void startRefresher(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                prices.refresh();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000);
    }

    private void createAndAdd(String name){
        portfolioList.put(name, new Portfolio(name));
    }

    @Override
    public Set<String> getPortfolioNames() {
        Set<String> foliosCopy = new HashSet<>();
        foliosCopy.addAll(portfolioList.keySet());
        return foliosCopy;
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
        boolean isSuccessful = false;
        if(portfolioList.containsKey(name)){
            portfolioList.remove(name);
            setChanged();
            notifyObservers(ViewUpdateType.DELETION);
            isSuccessful = true;
        }
        assert !portfolioList.containsKey(name): "Looking for:" + name + " in " + portfolioList.keySet();
        return isSuccessful;
    }

    @Override
    public Boolean createPortfolio(String name) {
        boolean isSuccessful = false;
        if(!portfolioList.containsKey(name)){
            createAndAdd(name);
            setChanged();
            notifyObservers(ViewUpdateType.CREATION);
            isSuccessful =  true;
        }
        assert portfolioList.containsKey(name): "Looking for:" + name + " in " + portfolioList.keySet();
        return isSuccessful;
    }

    @Override
    public Boolean savePortfolios(File file) {
        Collection<Portfolio> portfolioBefore = null;
        assert (portfolioBefore = new ArrayList<>()) != null;
        assert (portfolioBefore.addAll(portfolioList.values()));
        boolean isSuccessful = false;
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
            isSuccessful = true;
        } catch (IOException i) {}
        assert portfolioBefore.containsAll(portfolioList.values()): "portfoliosBefore:" + portfolioBefore + "\n portfoliosAfter:" + portfolioList.values();
        return isSuccessful;
    }


    @Override
    public Boolean loadPortfolioFromFile(File file) {
        System.setProperty("user.dir", file.getAbsolutePath());
        int portfoliosBefore = portfolioList.values().size();
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            try {
                while (true) {
                    Object inputObject = in.readObject();
                    if(inputObject instanceof Map){
                        this.prices = new Prices(new StrathQuoteServer(), (Map<String, Double>) inputObject);
                    }else if(inputObject instanceof Portfolio){
                        portfolioList.put(((Portfolio) inputObject).getPortfolioName(), (Portfolio) inputObject);
                    }else{
                        return false;
                    }
                }
            } catch (EOFException e) {
                in.close();
                fileIn.close();
                if(portfoliosBefore == portfolioList.values().size()){
                    return false;
                }
                setChanged();
                notifyObservers(ViewUpdateType.CREATION);
                return true;
            }
        } catch (Exception e) {
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
