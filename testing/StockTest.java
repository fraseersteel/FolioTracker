

import QuoteServer.MockQuoteServer;
import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class StockTest {

    private Stock stock;
    private Prices prices;

    @Before
    public void setUp(){
        prices = new Prices(new MockQuoteServer());
        stock = new Stock("BT","stock1",5);
    }

    @Test
    public void testGetTicketSymbol(){
        assertEquals("BT",stock.getTickerSymbol());
        System.out.println(stock.getTickerSymbol());
    }

    @Test
    public void testGetNumShares(){

    }

//    @Test
//    public void testOtherConstructor(){
//        Stock stock1 = new Stock("Bt","Name",10);
//        Stock stock3 = new Stock(stock1);
//        assertEquals("BT",stock3.getTickerSymbol());
//    }

    @Test
    public void testGetPricePerShare(){
        stock.setStockName("newName");
        assertEquals("newName",stock.getStockName());
    }


    @Test
    public void testInitialPricePerShare(){
        Double price = 12.34;
        assertEquals(price,stock.getInitalPricePerShare());
    }


    @Test
    public void testGetStockName(){
        assertEquals("BT",stock.getTickerSymbol());
    }


    @Test
    public void testBuyShares(){
        stock.buyShares(100);
        assertEquals(105,stock.getNumShares());
    }


    @Test
    public void testValueOfHolding(){
        Double expect = 61.7;
        assertEquals(expect,stock.getValueOfHolding());
    }

    @Test
    public void testProfitOfHolding(){
        Double expected = 0.00;
        assertEquals(expected,stock.getProfitOfHolding());
    }


    @Test
    public void testEqualsReflex() {
        assertEquals(stock, stock);
        assertEquals(stock.hashCode(), stock.hashCode());
    }

    @Test
    public void testEqualsDifClass() {
        assertNotEquals(stock, "");
    }

    @Test
    public void testEqualsSymetric() {
        Stock newStock = new Stock("BT", "stock1", 5);
        assertEquals(stock, newStock);
        assertEquals(newStock, stock);
        assertEquals(stock.hashCode(), newStock.hashCode());
    }

    @Test
    public void testEquals2() {
        Stock newStock = new Stock("NKE", "stock1", 5);
        assertNotEquals(stock, newStock);
    }

    @Test
    public void testEquals3() {
        Stock newStock = new Stock("BT", "stock2", 5);
        assertNotEquals(stock, newStock);
    }

    @Test
    public void testEquals4() {
        Stock newStock = new Stock("BT", "stock1", 10);
        assertNotEquals(stock, newStock);
    }

    @Test
    public void testEqualsSTransitive() {
        Stock newStock = new Stock("BT", "stock1", 5);
        Stock newStock2 = new Stock("BT", "stock1", 5);
        assertEquals(stock, newStock);
        assertEquals(newStock, newStock2);
        assertEquals(newStock2, stock);
        assertEquals(stock.hashCode(), newStock.hashCode());
        assertEquals(newStock2.hashCode(), newStock.hashCode());
    }

    @Test
    public void testEqualsConsist() {
        Stock newStock = new Stock("BT", "stock1", 5);
        assertEquals(stock, newStock);
        assertEquals(stock, newStock);
        assertEquals(stock.hashCode(), newStock.hashCode());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(null, stock);
    }

}

