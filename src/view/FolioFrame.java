package view;

import model.IPortfolioTracker;
import model.Prices;
//import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class FolioFrame extends JFrame implements Observer, IFolioFrame {


    private IPortfolioTracker portfolioTracker;

    private JPanel contentpane;
    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();
    private JTabbedPane tabbedPane;

    public FolioFrame(IPortfolioTracker portfolioTracker){
        this.portfolioTracker = portfolioTracker;
        contentpane = new JPanel();
        setContentPane(contentpane);
//        contentpane.setLayout(new BoxLayout(contentpane, BoxLayout.PAGE_AXIS));
//        contentpane.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));

        setupFrame();
        setupMenuBar();
        setupComponents();
        for(String name : portfolioTracker.getPortfolioNames()){
            insertProfile(name);
        }
    }

    private void setupFrame(){
        setLayout(new GridLayout(1, 1));
        setTitle("FolioTracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 550));
        pack();
        setVisible(true);
    }

    public void setupComponents() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
    }

    private void insertProfile(String name){
        StockTable table = new StockTable(portfolioTracker.getPortfolioByName(name));
        portfolioTracker.addObserverToPrices(table);
        profiles.put(name, table);
        tabbedPane.addTab(name, null, table,
                "Does nothing");
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Folio");
        menuBar.add(menu);

        JMenuItem open = new JMenuItem("Open");
        menu.add(open);

        JMenuItem options = new JMenuItem("Options");
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
