package model;

import model.IPortfolioTracker;

import javax.sound.sampled.Port;
import java.io.*;
import java.util.List;

public class PortfolioTracker implements IPortfolioTracker {

    private List<Portfolio> portfolioList;
    private List<Stock> stockList;
    private String fileName;

    public PortfolioTracker() {
    this.fileName = "folioTracker.config";
    }

    @Override
    public List<IPortfolio> getPortfolios() {
        return null;
    }

    @Override
    public IPortfolio getPortfolioByName(String name) {
        return null;
    }

    public List<Portfolio> getPortFoilioList() {
        return portfolioList;
    }

    public List<Stock> getStockList() {
    return stockList;
    }

    @Override
    public void savePortfolios() {
            try {
                FileOutputStream outPut = new FileOutputStream(fileName);
                ObjectOutputStream out = new ObjectOutputStream(outPut);
                for (Portfolio portfolio : portfolioList) {
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
                        portfolioList.add((Portfolio) inputObject);
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
    public void deletePortfolio(Portfolio portfolio) {
        getPortFoilioList().clear();
    }

}
