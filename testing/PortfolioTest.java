


import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import java.util.HashMap;
import java.util.Map;

public class PortfolioTest {

    private Portfolio portfolio;
    private Prices prices;

    @Before
    public void setUp() {
        prices = new Prices(getPricesMap());
        portfolio = new Portfolio("test");
    }

    @After
    public void closeDown() {
        prices = new Prices(getPricesMap());
    }

    private Map<String, Double> getPricesMap() {
        Map<String, Double> somePrices = new HashMap<>();
        somePrices.put("BT", 16.68);
        somePrices.put("NKE", 15.12);

        return somePrices;
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
        assertEquals(compare, prices.getCurrentPriceList());
    }

//    @Test
//    public void testBuyStock(){
//        assertTrue(portfolio.buyStock("GOLD",100));
//    }


    @Test
    public void testBuyNull() {
       assertFalse(portfolio.buyStock("bt",100));
    }

    @Test
    public void testSetNameOfStock() {
        portfolio.buyStock("BT", 100);
        assertTrue(portfolio.setNameOfStock("BT", "bob"));
    }

    @Test
    public void testSetNameAlreadyTaken() {
        assertFalse(portfolio.setNameOfStock("BT", "BTNAME"));
    }

    @Test
    public void testSellStockEqualToShares() {
        portfolio.buyStock("bt", 100);
        assertTrue(portfolio.sellStock("bt", 100));
    }

    @Test
    public void testSellStockLessToShares() {
        portfolio.buyStock("bt", 100);
        assertTrue(portfolio.sellStock("bt", 50));
    }

    @Test
    public void testSellMoreThanOwned() {
        portfolio.buyStock("bt", 100);
        assertFalse(portfolio.sellStock("bt", 200));
    }

    @Test
    public void noPortfolio() {
        assertFalse(portfolio.sellStock("Doesnt Exist", 00));
    }


}


