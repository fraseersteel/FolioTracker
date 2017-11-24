package testing;

import static org.junit.Assert.*;

import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PortfolioTest {

    private Portfolio portfolio;
    private Prices prices;

    @Before
    public void setUp(){
        prices = new Prices(getPricesMap());

        portfolio = new Portfolio("test");
    }

    private Map<String, Double> getPricesMap(){
        Map<String, Double> somePrices = new HashMap<>();
        somePrices.put("BT",16.68);
        somePrices.put("NKE",15.12);

        return somePrices;
    }

    @Test
    public void testGetPortfolioName(){
     assertEquals("test", portfolio.getPortfolioName());
    }

//    @Test
//    public void testGetStockTickers(){
//
//    }

//    @Test
//    public void testGetStockByTicker(){
//
//    }

    @Test
    public void testBuyStock(){
        Portfolio p = new Portfolio("new");
        assertTrue(p.buyStock("bt",100));
//        assertTrue(portfolio.buyStock("bt",100));
    }

    @Test
    public void testSellStock(){
        portfolio.buyStock("bt",100);
        assertTrue(portfolio.sellStock("bt",100));
    }


}


