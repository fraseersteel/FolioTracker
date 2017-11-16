package testing;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Portfolio;
import org.junit.Before;
import org.junit.jupiter.api.Test;

public class PortfolioTest {

 Portfolio portfolio;

    @Before
    public void setUP() {
        portfolio = new Portfolio("test");
    }

    @Test
    public void testRemove() {
        assertTrue(portfolio.removeStock("1"));
    }


    @Test
    public void testCreate(){
        Portfolio portfolioTest = new Portfolio("bt");
        portfolioTest.createStock("bt",1);
        assertTrue(portfolioTest.removeStock("bt"));
    }


    @Test
    public void testChangeName() {
        System.out.println(portfolio.getPortfolioName());
   portfolio.setPortfolioName("newName");
        System.out.println(portfolio.getPortfolioName());
    assertEquals("newName",portfolio.getPortfolioName());
}


}
