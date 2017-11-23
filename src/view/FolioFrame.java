package view;

import controller.PortfolioListener;
import model.IPortfolio;
import model.IPortfolioTracker;
import model.Prices;
import model.ViewUpdateType;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
//import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class FolioFrame extends JFrame implements Observer, IFolioFrame {

//    private JPanel contentpane;
    private JTabbedPane tabbedPane;

    private IPortfolioTracker portfolioTracker;
    private Set<String> hiddenFolios = new HashSet<>();
    private Map<String, StockTable> profiles = new HashMap<>();

    public FolioFrame(IPortfolioTracker portfolioTracker){
        this.portfolioTracker = portfolioTracker;
//        contentpane = new JPanel();
//        setContentPane(contentpane);
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
        Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = 900;
        int frameHeight = 550;

        int frameX = SCREEN_DIMENSION.width / 2 - frameWidth / 2;
        int frameY = SCREEN_DIMENSION.height / 2 - frameHeight / 2;
        setBounds(frameX, frameY, frameWidth, frameHeight);
//        pack();
        setVisible(true);
    }

    private void insertProfile(String name){
        StockTable table = new StockTable(portfolioTracker.getPortfolioByName(name));
        portfolioTracker.addObserverToFolio(name, table);
        portfolioTracker.addObserverToPrices(table);
        profiles.put(name, table);
        tabbedPane.addTab(name, null, table,
                "");
    }

    private void removeProfile(String name){
        tabbedPane.remove(profiles.get(name));
        profiles.remove(name);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        ActionListener listener = new PortfolioListener(portfolioTracker, this);
        JMenuItem load = new JMenuItem("Load Folio From File");
        load.addActionListener(listener);
        file.add(load);

        JMenuItem save = new JMenuItem("Save Folios");
        save.addActionListener(listener);
        file.add(save);

        JMenu edit = new JMenu("Folio Actions");
        menuBar.add(edit);

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(listener);
        edit.add(open);

        JMenuItem hide = new JMenuItem("Hide");
        hide.addActionListener(listener);
        edit.add(hide);

        edit.addSeparator();

        JMenuItem newFolio = new JMenuItem("New");
        newFolio.addActionListener(listener);
        edit.add(newFolio);
        JMenuItem delete = new JMenuItem("Delete");
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
            refreshFolioTabs();
        }
    }

    private void refreshFolioTabs(){
        for(String name : portfolioTracker.getPortfolioNames()){
            if(!profiles.containsKey(name) && !hiddenFolios.contains(name)){
                insertProfile(name);
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

    @Override
    public boolean hideSelectedFolio() {
        int i = tabbedPane.getSelectedIndex();
        if(i != -1){
            String name = tabbedPane.getTitleAt(i);
            hiddenFolios.add(name);
            tabbedPane.removeTabAt(i);
            profiles.remove(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean showFolio(String name){
        if(hiddenFolios.contains(name)){
            hiddenFolios.remove(name);
            refreshFolioTabs();
            return true;
        }
        return false;
    }
}
