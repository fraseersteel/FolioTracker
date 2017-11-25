import model.Portfolio;
import model.PortfolioTracker;
import model.Prices;
import model.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PortfolioTrackerTest {

    PortfolioTracker portfolioTracker;
    File file;

    @Before
    public void setUp(){
    portfolioTracker = new PortfolioTracker();
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

}
