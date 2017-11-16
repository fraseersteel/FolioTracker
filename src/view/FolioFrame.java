package view;

import model.IPortfolio;
import model.IPortfolioTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class FolioFrame extends JFrame implements Observer {

    private JPanel contentPane;
    private JTabbedPane tabbedPane;

    private IPortfolioTracker model;
    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();

    // Used to get currently selected portfolio
    private List<IPortfolio> portfolios;

    public FolioFrame() {
        contentPane = new JPanel();
        setContentPane(contentPane);
        //contentPane.setLayout(new BorderLayout());

        portfolios = new ArrayList<>();

        setupFrame();
        setupMenuBar();
        setupComponents();




//        contentpane.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));


        // setupMenuBar();
        // setupComponents();
        //insertProfile("Test1", new StockTable());
        //StockTable table = new StockTable();
        //table.insertValues("hi", "there", 30, 1.0);
        // insertProfile("Test2", table);
    }

    private void setupFrame() {
        setLayout(new GridLayout(1, 1));
        setTitle("FolioTracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 550));
        pack();
        setVisible(true);
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

    public void setupComponents() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
    } 

    //todo come back to tab system, concurrency? results in errors in test driver with 500ms delay between adding
    // fine with 5000ms
    //should rarely happen
    //less efficient deleting panes
    //cleaner for reloading save

    //could use method to delete
    public void setTabbedPane(List<IPortfolio> newPortfolios) {
        //store portfolio name, or id or smth
        IPortfolio currentlySelectedFolio = null;

        if (portfolios.size() > 0 && tabbedPane.getSelectedIndex() >= 0 && tabbedPane.getSelectedIndex() < portfolios.size()) {
            currentlySelectedFolio = portfolios.get(tabbedPane.getSelectedIndex());
        }

        portfolios = new ArrayList<>();
        tabbedPane.setSelectedIndex(-1);
        tabbedPane.removeAll();

        int index = 0;
        for (IPortfolio i : newPortfolios) {
            StockTable table;
            if (!profiles.containsKey(i.getPortfolioName())) {
                table = new StockTable();
                profiles.put(i.getPortfolioName(), table);
            } else {
                table = profiles.get(i.getPortfolioName());
            }


            table.insertValues("hi", "there", index, 1.0);
            //todo
            portfolios.add(i);
            tabbedPane.addTab(i.getPortfolioName(), null, table);


            //todo can only be tested properly after hide/unhide, loading from save as thats when folio list can change
            //(presumably making a new folio would set the index to that)
            if (i == currentlySelectedFolio) {
                tabbedPane.setSelectedIndex(index);
            }

            index++;
        }
    }




    @Override
    public void update(Observable o, Object arg) {
        // updates to the folios?
    }
}
