package testing;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Portfolio;
import model.Prices;
import org.junit.jupiter.api.Test;
import org.junit.Before;

public class PortfolioTest {

    Portfolio portfolio;
    Prices prices = new Prices();


    @Before
    public void setUP() {
        portfolio = new Portfolio("test");
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


