

import model.Portfolio;
import model.Prices;
import model.Stock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
        Double price = 16.68;
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
        Double expect = 83.4;
        assertEquals(expect,stock.getValueOfHolding());
    }

    @Test
    public void testProfitOfHolding(){
        Double expected = 0.00;
        assertEquals(expected,stock.getProfitOfHolding());
    }

}

