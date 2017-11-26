import QuoteServer.MockQuoteServer;
import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import static org.junit.Assert.*;


public class PortfolioTrackerTest {

    PortfolioTracker portfolioTracker;
    File file;

    @Before
    public void setUp(){
    portfolioTracker = new PortfolioTracker(new MockQuoteServer());
    portfolioTracker.createPortfolio("JUnitTest");
    file = new File("Test");
    }

    @After
    public void closeDown(){
        portfolioTracker = null;
    }

    @Test
    public void deletePortfolioByNameDoesntExist(){
        assertFalse(portfolioTracker.deletePortfolioByName("this should fail"));
    }

    @Test
    public void deletePortfolioByNameExist(){
        assertTrue(portfolioTracker.deletePortfolioByName("JUnitTest"));
    }

    @Test
    public void savePortfolio(){
    assertTrue(portfolioTracker.savePortfolios(file));
    }

    @Test
    public void loadPortfolioFromFile(){
        assertTrue(portfolioTracker.loadPortfolioFromFile(file));
    }

    @Test
    public void loadPortfolioFromFileWithString(){
        try {
            FileOutputStream outPut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(outPut);
            out.writeObject(new String());
            out.close();
            outPut.close();
            assertFalse(portfolioTracker.loadPortfolioFromFile(file));
        } catch (IOException e) {
        }

    }

    @Test
    public void loadMultiplePortfoliosFromFile(){
        portfolioTracker.createPortfolio("Fraser");
        portfolioTracker.createPortfolio("Test");
        portfolioTracker.savePortfolios(file);
        portfolioTracker.loadPortfolioFromFile(file);
    }

    @Test
    public void testRefresh(){
        portfolioTracker.startRefresher();
        assertTrue(Thread.currentThread().isAlive());
    }


    @Test
    public void testgetPortfolioNames(){
        assertTrue(portfolioTracker.getPortfolioNames().contains("JUnitTest"));
        assertEquals(1, portfolioTracker.getPortfolioNames().size());
        assertFalse(portfolioTracker.getPortfolioNames().contains("testing"));
    }

    @Test
    public void testgetPortfolioByName(){
        assertNotNull(portfolioTracker.getPortfolioByName("JUnitTest"));
        assertNull(portfolioTracker.getPortfolioByName("WHAT"));
    }

    @Test
    public void testaddObserverToPrices(){

    }
}
