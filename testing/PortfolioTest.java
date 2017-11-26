


import QuoteServer.MockQuoteServer;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import javax.sound.sampled.Port;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PortfolioTest {

    private Portfolio portfolio;
    private Prices prices;

    @Before
    public void setUp() {
        prices = new Prices(new MockQuoteServer());
        portfolio = new Portfolio("test");
    }

    @After
    public void closeDown() {
        prices = null;
    }

    @Test
    public void testGetPortfolioName() {
        assertEquals("test", portfolio.getPortfolioName());
    }

    @Test
    public void testGetStockTickers() {
        Map<String, Double> compare = new HashMap<>();
        compare.put("BT", 16.68);
        compare.put("NKE", 15.12);
        compare.put("A", 12.11);
        assertNotEquals(compare, prices.getCurrentPriceList());
    }

//    @Test
//    public void testBuyStock(){
//        assertTrue(portfolio.buyStock("GOLD",100));
//    }


    @Test
    public void testBuyNull() {
        assertFalse(portfolio.buyStock("GOLD", 100));
    }

    @Test
    public void testSetNameOfStock() {
        portfolio.buyStock("A", 100);
        assertTrue(portfolio.setNameOfStock("A", "bob"));
    }

    @Test
    public void testEncapsulateTickers() {
        portfolio.buyStock("BT", 100);
        Set<String> tickers = portfolio.getStockTickers();
        for (String name : tickers) {
            tickers.remove(name);
        }
        assertFalse(tickers.containsAll(portfolio.getStockTickers()));
    }

    @Test
    public void testSetNameAlreadyTaken() {
        assertFalse(portfolio.setNameOfStock("BT", "BTNAME"));
    }

    @Test
    public void testSellStockEqualToShares() {
        portfolio.buyStock("BT", 100);
        assertTrue(portfolio.sellStock("BT", 100));
    }

    @Test
    public void testSellStockLessToShares() {
        portfolio.buyStock("BT", 100);
        assertTrue(portfolio.sellStock("BT", 50));
    }

    @Test
    public void testSellMoreThanOwned() {
        portfolio.buyStock("A", 100);
        assertFalse(portfolio.sellStock("A", 200));
    }

    @Test
    public void testBuyMore() {

        assertTrue(portfolio.buyStock("A", 100));
        assertTrue(portfolio.buyStock("A", 100));
    }

    @Test
    public void noPortfolio() {
        assertNull(portfolio.sellStock("Doesnt Exist", 00));
    }


    @Test
    public void testEqualsReflex() {
        assertEquals(portfolio, portfolio);
        assertEquals(portfolio.hashCode(), portfolio.hashCode());
    }

    @Test
    public void testEqualsSymetric() {
        Portfolio newPort = new Portfolio("test");
        newPort.buyStock("A", 100);
        portfolio.buyStock("A", 100);
        assertEquals(portfolio, newPort);
        assertEquals(newPort, portfolio);
        assertEquals(portfolio.hashCode(), newPort.hashCode());
    }

    @Test
    public void testEqualsSymetric2() {
        Portfolio newPort = new Portfolio("test");
        newPort.buyStock("BT", 100);
        portfolio.buyStock("A", 100);
        assertNotEquals(portfolio, newPort);
    }

    @Test
    public void testEqualsSymetric3() {
        Portfolio newPort = new Portfolio("one");
        assertNotEquals(portfolio, newPort);
    }

    @Test
    public void testEqualsSymetric4() {
        Portfolio newPort = new Portfolio("test");
        newPort.buyStock("BT", 100);
        assertNotEquals(portfolio, newPort);
    }

    @Test
    public void testEqualsSTransitive() {
        Portfolio newPort = new Portfolio("test");
        newPort.buyStock("NKE", 100);
        portfolio.buyStock("NKE", 100);
        Portfolio sndPort = new Portfolio("test");
        sndPort.buyStock("NKE", 100);
        assertEquals(portfolio, newPort);
        assertEquals(newPort, sndPort);
        assertEquals(sndPort, portfolio);
        assertEquals(portfolio.hashCode(), newPort.hashCode());
        assertEquals(sndPort.hashCode(), newPort.hashCode());
    }

    @Test
    public void testEqualsConsist() {
        Portfolio newPort = new Portfolio("test");
        newPort.buyStock("BT", 100);
        portfolio.buyStock("BT", 100);
        assertEquals(portfolio, newPort);
        assertEquals(portfolio, newPort);
        assertEquals(portfolio.hashCode(), newPort.hashCode());
    }

    @Test
    public void testEqualsNotSameInstance() {
        assertNotEquals(ViewUpdateType.CREATION, portfolio);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(null, portfolio);
    }
}


