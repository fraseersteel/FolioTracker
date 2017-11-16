package testing;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


import model.Stock;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class StockTest {
   private Stock stock = new Stock("bt","stock1",1,10.5,10.5);

    @Before
    public void setUp(){
    }

    @Test
    public void testGetTicketSymbol(){
        assertEquals("bt",stock.getTicketSymbol());
        System.out.println(stock.getTicketSymbol());
    }


    @Test
    public void testDeleteStock(){
        //
    }


    @Test
    public void testSetPricePerShare(){
        stock.setPricePerShare(100);
        assertEquals(100,stock.getPricePerShare());
    }


    @Test
    public void setStockName(){
        stock.setStockName("newName");
        assertEquals("newName",stock.getStockName());
    }


    @Test
    public void setValueOfHolding(){
        stock.setValueOfHolding(2000);
        assertEquals(2000,stock.getValueOfHolding());
    }



}
