package view;

import model.IPortfolioTracker;
import model.Prices;
//import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class FolioFrame extends JFrame implements Observer, IFolioFrame {


    private IPortfolioTracker portfolioTracker;

    private JPanel contentpane;
    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();
    private JTabbedPane tabbedPane = new JTabbedPane();

    public FolioFrame(IPortfolioTracker portfolioTracker){
        setTitle("FolioTracker");
        this.portfolioTracker = portfolioTracker;
        contentpane = new JPanel();
        contentpane.setLayout(new BoxLayout(contentpane, BoxLayout.PAGE_AXIS));
//        contentpane.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));

        initMenuBar();
        initComponents();
        setUpFrame();
        for(String name : portfolioTracker.getPortfolioNames()){
            insertProfile(name);
        }
    }

    private void setUpFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 825, 400);
        setVisible(true);
        this.setContentPane(contentpane);
    }

    private void insertProfile(String name){
        StockTable table = new StockTable(portfolioTracker.getPortfolioByName(name));
        portfolioTracker.addObserverToPrices(table);
        profiles.put(name, table);
        tabbedPane.addTab(name, null, table,
                "Does nothing");
    }

    private void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Folio");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem options = new JMenuItem("Options");
        menuBar.add(menu);
        menu.add(open);
        menu.add(options);
        setJMenuBar(menuBar);
    }

    private void initComponents(){
        contentpane.add(tabbedPane);
    }


    @Override
    public void update(Observable o, Object arg) {
        // updates to the folios?
    }
}
