import QuoteServer.MockQuoteServer;
import model.Portfolio;
import model.Prices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PricesTest {

    private Prices prices;

    @Before
    public void setUp() {
        prices = new Prices(new MockQuoteServer());
    }

    @After
    public void closeDown() {
        prices = null;
    }

    @Test
    public void testTickerNotrecognised() {
        assertFalse(Prices.addTicker("WHY"));
    }

    @Test
    public void testTickerRecognised() {
        assertTrue(Prices.addTicker("BT"));
    }

    @Test
    public void testGetTicker() {
        assertNotNull(Prices.getPriceOfTicker("BT"));
    }

    @Test
    public void testGetNonTicker() {
        assertNull(Prices.getPriceOfTicker("WHY"));
    }

    @Test
    public void testRefreshAndAdd() {
        prices.refresh();
        assertTrue(Prices.addTicker("BT"));
    }
}


