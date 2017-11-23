package testing;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Portfolio;
import model.Stock;
import org.junit.jupiter.api.Test;
import org.junit.Before;


public class StockTest {

    private Stock stock;

    @Before
    public void setUp(){
        stock = new Stock("bt","stock1",5);
    }

    @Test
    public void testGetTicketSymbol(){
        assertEquals("bt",stock.getTickerSymbol());
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

