package testing;

import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;


public class StockTest {

    private Stock stock;
    private Prices prices;

    @Before
    public void setUp(){
        prices = new Prices(getPricesMap());

        stock = new Stock("BT","stock1",5);
    }

    private Map<String, Double> getPricesMap(){
        Map<String, Double> somePrices = new HashMap<>();
        somePrices.put("BT",16.68);
        somePrices.put("NKE",15.12);

        return somePrices;
    }

    @Test
    public void testGetTicketSymbol(){
        assertEquals("BT",stock.getTickerSymbol());
        System.out.println(stock.getTickerSymbol());
    }

    @Test
    public void testGetNumShares(){

    }


    @Test
    public void testGetPricePerShare(){
        stock.setStockName("newName");
        assertEquals("newName",stock.getStockName());
    }


    @Test
    public void testInitialPricePerShare(){

    }

    @Test
    public void testProfitOfHolding(){

    }

    @Test
    public void testGetStockName(){

    }

}

