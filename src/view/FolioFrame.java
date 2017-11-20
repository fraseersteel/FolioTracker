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
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class FolioFrame extends JFrame implements Observer, IFolioFrame {

    private JPanel contentpane;
    private JTabbedPane tabbedPane;

    private IPortfolioTracker portfolioTracker;

    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();

    public FolioFrame(IPortfolioTracker portfolioTracker){
        this.portfolioTracker = portfolioTracker;
        contentpane = new JPanel();
        setContentPane(contentpane);
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
