package view;

import controller.PortfolioListener;
import model.IPortfolio;
import model.IPortfolioTracker;
import model.Prices;
import model.ViewUpdateType;
//import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.*;
import java.util.List;

public class FolioFrame extends JFrame implements Observer, IFolioFrame {

    private JPanel contentpane;
    private JTabbedPane tabbedPane;

    private IPortfolioTracker portfolioTracker;

    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();

    // Used to get currently selected portfolio
    private List<IPortfolio> portfolios;

    public FolioFrame(IPortfolioTracker portfolioTracker){
        this.portfolioTracker = portfolioTracker;
        contentpane = new JPanel();
        setContentPane(contentpane);
//        contentpane.setLayout(new BoxLayout(contentpane, BoxLayout.PAGE_AXIS));
//        contentpane.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));
            portfolios = new ArrayList<>();
        setupFrame();
        setupMenuBar();
        setupComponents();

        // setupMenuBar();
        // setupComponents();
        //insertProfile("Test1", new StockTable());
        //StockTable table = new StockTable();
        //table.insertValues("hi", "there", 30, 1.0);
        // insertProfile("Test2", table);

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

    private void insertProfile(String name){
        StockTable table = new StockTable(portfolioTracker.getPortfolioByName(name));
        portfolioTracker.addObserverToFolio(name, table);
        portfolioTracker.addObserverToPrices(table);
        profiles.put(name, table);
        tabbedPane.addTab(name, null, table,
                "Does nothing");
    }

    private void removeProfile(String name){
        tabbedPane.remove(profiles.get(name));
        profiles.remove(name);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        PortfolioListener listener = new PortfolioListener(portfolioTracker, this);
        JMenuItem open = new JMenuItem("Open Folio From File");
        open.addActionListener(listener);
        file.add(open);

        JMenuItem save = new JMenuItem("Save Folios");
        save.addActionListener(listener);
        file.add(save);

        JMenu edit = new JMenu("Edit");
        menuBar.add(edit);

        JMenuItem newFolio = new JMenuItem("New Folio");
        newFolio.addActionListener(listener);
        edit.add(newFolio);
        JMenuItem delete = new JMenuItem("Delete Folio");
        delete.addActionListener(listener);
        edit.add(delete);

        setJMenuBar(menuBar);
    }

    private void setupComponents() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
    }

    //todo come back to tab system, concurrency? results in errors in test driver with 500ms delay between adding
    // fine with 5000ms
    //should rarely happen
    //less efficient deleting panes
    //cleaner for reloading save

    //could use method to delete
    //potentially unreliable, use insertprofile for time being
//    public void setTabbedPane(List<IPortfolio> newPortfolios) {
//        //store portfolio name, or id or smth
//        IPortfolio currentlySelectedFolio = null;
//
//        if (portfolios.size() > 0 && tabbedPane.getSelectedIndex() >= 0 && tabbedPane.getSelectedIndex() < portfolios.size()) {
//            currentlySelectedFolio = portfolios.get(tabbedPane.getSelectedIndex());
//        }
//
//        portfolios = new ArrayList<>();
//        tabbedPane.setSelectedIndex(-1);
//        tabbedPane.removeAll();
//
//        int index = 0;
//        for (IPortfolio i : newPortfolios) {
//            StockTable table;
//            if (!profiles.containsKey(i.getPortfolioName())) {
//                table = new StockTable(i);
//                profiles.put(i.getPortfolioName(), table);
//            } else {
//                table = profiles.get(i.getPortfolioName());
//            }
//
//            portfolios.add(i);
//            tabbedPane.addTab(i.getPortfolioName(), null, table);
//
//
//            //todo can only be tested properly after hide/unhide, loading from save as thats when folio list can change
//            //(presumably making a new folio would set the index to that)
//            if (i == currentlySelectedFolio) {
//                tabbedPane.setSelectedIndex(index);
//            }
//
//            index++;
//        }
//    }

    @Override
    public void update(Observable o, Object arg) {
        if (ViewUpdateType.DELETION.equals(arg)) {

            Set<String> namesNotSeen = new HashSet<>();
            namesNotSeen.addAll(profiles.keySet());
            for(String name : portfolioTracker.getPortfolioNames()){
                namesNotSeen.remove(name);
            }
            for(String folioName : namesNotSeen){
                removeProfile(folioName);
            }

        } else if (ViewUpdateType.CREATION.equals(arg)) {

            for(String name : portfolioTracker.getPortfolioNames()){
                if(!profiles.containsKey(name)){
                    insertProfile(name);
                }
            }
        }
    }

    @Override
    public String getSelectedFolio() {
        int i = tabbedPane.getSelectedIndex();
        if(i != -1){
            System.out.println(i);
            return tabbedPane.getTitleAt(i);
        }
        return null;
    }
}
